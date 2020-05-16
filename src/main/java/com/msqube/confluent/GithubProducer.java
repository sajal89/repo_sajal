package com.msqube.confluent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.mashape.unirest.http.JsonNode;
import com.msqube.confluent.model.ProducerModel;
import com.msqube.confluent.model.Repository;
import com.msqube.confluent.model.User;

@Component
public class GithubProducer {

	private static final String OWNER_ORG = "owner.org";
	private static final String OWNER_TOKEN = "owner.token";
	private static final String SECURITY_PROTOCOL = "security.protocol";
	private static final String VALUE_SERIALIZER = "value.serializer";
	private static final String KEY_SERIALIZER = "key.serializer";
	private static final String OWNER_LOGIN = "owner.login";
	private static final String TOPIC_KEY = "githubRepo";
	private static final String KAFKA_TOPIC = "kafka.topic";
	private static final String BOOTSTRAP_SERVERS = "bootstrap.servers";
	private static final String CLIENT_ID = "client.id";
	private static final String ALGO = "ssl.endpoint.identification.algorithm";
	private static final String SASL_MECHANISM = "sasl.mechanism";
	private static final String REQST_TIMEOUT = "request.timeout.ms";
	private static final String RETRY_BACKOFF = "retry.backoff.ms";
	private static final String JAAS_CONFIG = "sasl.jaas.config";

	private static final Logger log = LoggerFactory.getLogger(GithubProducer.class);

	@Autowired
	private Environment env;
	@Autowired
	private GitHubAPIHttpClient gitClient;

	private KafkaProducer<String, ProducerModel> producer = null;

	private void initializeVaraiables() {


		Properties config = new Properties();
		try {
			config.put(CLIENT_ID, InetAddress.getLocalHost().getHostName());

			config.put(BOOTSTRAP_SERVERS, env.getProperty(BOOTSTRAP_SERVERS));
			config.put("acks", "all");
			config.put(KEY_SERIALIZER, env.getProperty(KEY_SERIALIZER));
			config.put(VALUE_SERIALIZER, env.getProperty(VALUE_SERIALIZER));
			config.put(ALGO, env.getProperty(ALGO));
			config.put(SASL_MECHANISM, env.getProperty(SASL_MECHANISM));
			config.put(REQST_TIMEOUT, env.getProperty(REQST_TIMEOUT));
			config.put(RETRY_BACKOFF, env.getProperty(RETRY_BACKOFF));
			config.put(JAAS_CONFIG, env.getProperty(JAAS_CONFIG));
			config.put(SECURITY_PROTOCOL, env.getProperty(SECURITY_PROTOCOL));

			log.info("Config Values: ", config.toString());
			producer = new KafkaProducer<>(config);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public void repoProducer() {

		// initialize variables and create kafka producer
		initializeVaraiables();

		try {
				//Repository producer
				String requestUrl = String.format("https://api.github.com/orgs/%s/repos", env.getProperty(OWNER_ORG));
				JsonNode jsonResponse;
				jsonResponse = gitClient.getNextItems(requestUrl,env.getProperty(OWNER_TOKEN));
				for (Object obj : jsonResponse.getArray()) {
					Repository repo = (Repository) (new Repository()).formJson((JSONObject) obj);
					repo.setOrganizationName(env.getProperty(OWNER_ORG));
					// push repository into topic
					sendMessage(repo);
				}
				
				//User producer
				requestUrl = String.format("https://api.github.com/orgs/%s/members", env.getProperty(OWNER_ORG));
				jsonResponse = gitClient.getNextItems(requestUrl,env.getProperty(OWNER_TOKEN));
				for (Object obj : jsonResponse.getArray()) {
					JSONObject userObj = (JSONObject) obj;
					String userLogin=userObj.getString("login");
					String userUrl = String.format("https://api.github.com/users/%s", userLogin);
					JsonNode userResponse = gitClient.getNextItems(userUrl,env.getProperty(OWNER_TOKEN));
					User user = (User) (new User()).formJson((JSONObject) userResponse.getObject());
					user.setOrganizationName(env.getProperty(OWNER_ORG));
					// push repository into topic
					sendMessage(user);
				}

		} catch (InterruptedException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} 

	}
	

	// send Message to kafka
	private void sendMessage(ProducerModel model) {
		try {
			if (model != null) {
				ProducerRecord<String, ProducerModel> record = new ProducerRecord<>(env.getProperty(KAFKA_TOPIC),
						TOPIC_KEY, model);
				RecordMetadata metadata = producer.send(record).get();
				log.info("Produced Message: ", metadata.offset());
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}
