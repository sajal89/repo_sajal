package com.msqube.confluent.model;

import java.time.Instant;
import org.json.JSONObject;

public class Repository implements ProducerModel  {
	private String node_type = "REPOS";

	private int repo_id;
	private int owner_id;
	private String name;
	private String organizationName;
	private String html_url;
	private String description;
	private String language;
	private String codeLanguages;
	private int forks_count;
	private int watchers_count;
	private int subscribers_count;
	private int reposize;
	private int open_issues_count;
	private boolean has_issues;
	private boolean has_projects;
	private boolean archived;
	private boolean disabled;
	private boolean visibility;
	private boolean isPrivate;
	private long pushed_at;
	private long created_at;
	
	public String getNode_type() {
		return node_type;
	}

	public void setNode_type(String node_type) {
		this.node_type = node_type;
	}

	public int getRepo_id() {
		return repo_id;
	}

	public void setRepo_id(int repo_id) {
		this.repo_id = repo_id;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganizationName() {
		return organizationName;
	}

	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}

	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCodeLanguages() {
		return codeLanguages;
	}

	public void setCodeLanguages(String codeLanguages) {
		this.codeLanguages = codeLanguages;
	}

	public int getForks_count() {
		return forks_count;
	}

	public void setForks_count(int forks_count) {
		this.forks_count = forks_count;
	}

	public int getWatchers_count() {
		return watchers_count;
	}

	public void setWatchers_count(int watchers_count) {
		this.watchers_count = watchers_count;
	}

	public int getSubscribers_count() {
		return subscribers_count;
	}

	public void setSubscribers_count(int subscribers_count) {
		this.subscribers_count = subscribers_count;
	}

	public int getReosize() {
		return reposize;
	}

	public void setRepoSize(int reposize) {
		this.reposize = reposize;
	}

	public int getOpen_issues_count() {
		return open_issues_count;
	}

	public void setOpen_issues_count(int open_issues_count) {
		this.open_issues_count = open_issues_count;
	}

	public boolean isHas_issues() {
		return has_issues;
	}

	public void setHas_issues(boolean has_issues) {
		this.has_issues = has_issues;
	}

	public boolean isHas_projects() {
		return has_projects;
	}

	public void setHas_projects(boolean has_projects) {
		this.has_projects = has_projects;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isVisibility() {
		return visibility;
	}

	public void setVisibility(boolean visibility) {
		this.visibility = visibility;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public long getPushed_at() {
		return pushed_at;
	}

	public void setPushed_at(long pushed_at) {
		this.pushed_at = pushed_at;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}


	public ProducerModel formJson(JSONObject jsonObject) {
		Repository repo = new Repository();
		
		repo.setRepo_id(jsonObject.getInt("id"));
		repo.setOwner_id(jsonObject.getJSONObject("owner").getInt("id"));
		repo.setName(jsonObject.getString("name"));
		repo.setHtml_url(jsonObject.getString("html_url"));
		repo.setForks_count(jsonObject.getInt("forks_count"));
		repo.setWatchers_count(jsonObject.getInt("watchers_count"));
		repo.setOpen_issues_count(jsonObject.getInt("open_issues_count"));
		repo.setRepoSize(jsonObject.getInt("size"));
		repo.setHas_issues(jsonObject.getBoolean("has_issues"));
		repo.setHas_projects(jsonObject.getBoolean("has_projects"));
		repo.setArchived(jsonObject.getBoolean("archived"));
		repo.setDisabled(jsonObject.getBoolean("disabled"));
		repo.setPrivate(jsonObject.getBoolean("private"));
		repo.setHas_issues(jsonObject.getBoolean("has_issues"));
		
		if (!jsonObject.isNull("description"))
			repo.setDescription(jsonObject.getString("description"));
		if (!jsonObject.isNull("language"))
			repo.setLanguage(jsonObject.getString("language"));
		
		
		long crtd = Instant.parse(jsonObject.getString("created_at")).getEpochSecond();
		repo.setCreated_at(crtd * 1000);
		crtd = Instant.parse(jsonObject.getString("pushed_at")).getEpochSecond();
		repo.setPushed_at(crtd * 1000);
		return repo;

	}

}
