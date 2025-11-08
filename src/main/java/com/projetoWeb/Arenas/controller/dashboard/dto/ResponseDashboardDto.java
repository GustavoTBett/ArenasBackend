package com.projetoWeb.Arenas.controller.dashboard.dto;

import lombok.Builder;

@Builder
public class ResponseDashboardDto {

  private Long id;
  private Long createUserId;
  private String createUserName;
  private String title;
  private String time;
  private String date;
  private Long maxPlayers;
  private Long currentPlayers;
  private String status;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getCreateUserId() {
    return createUserId;
  }

  public void setCreateUserId(Long createUserId) {
    this.createUserId = createUserId;
  }

  public String getCreateUserName() {
    return createUserName;
  }

  public void setCreateUserName(String createUserName) {
    this.createUserName = createUserName;
  }

  public Long getMaxPlayers() {
    return maxPlayers;
  }

  public void setMaxPlayers(Long maxPlayers) {
    this.maxPlayers = maxPlayers;
  }

  public Long getCurrentPlayers() {
    return currentPlayers;
  }

  public void setCurrentPlayers(Long currentPlayers) {
    this.currentPlayers = currentPlayers;
  }

}
