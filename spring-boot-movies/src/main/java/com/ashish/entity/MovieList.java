package com.ashish.entity;


public class MovieList {

	private Integer page;
	private Integer total_res;
	private Integer total_page;
	private Movie [] results;
	
	public MovieList()
	{
		
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getTotal_res() {
		return total_res;
	}

	public void setTotal_res(Integer total_res) {
		this.total_res = total_res;
	}

	public Integer getTotal_page() {
		return total_page;
	}

	public void setTotal_page(Integer total_page) {
		this.total_page = total_page;
	}

	public Movie [] getResults() {
		return results;
	}

	public void setResults(Movie [] results) {
		this.results = results;
	}
	
}
