package com.projetoWeb.Arenas.controller.match.dto;

import lombok.Builder;

@Builder
public class ResponseSearchMatchDto {

  private Long id;
  private Long createUserId;
  private String createUserName;
  private String title;
  private String time;
  private String date;
  private Long maxPlayers;
  private Long currentPlayers;
  private String status;
  private String localName;
  private String localZipCode;
  private String localStreet;
  private String localNumber;
  private String localComplement;
  private String localCity;
  private String localState;
  private String localNeighborhood;

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

  public String getLocalName() {
    return localName;
  }

  public void setLocalName(String localName) {
    this.localName = localName;
  }

  public String getLocalZipCode() {
    return localZipCode;
  }

  public void setLocalZipCode(String localZipCode) {
    this.localZipCode = localZipCode;
  }

  public String getLocalStreet() {
    return localStreet;
  }

  public void setLocalStreet(String localStreet) {
    this.localStreet = localStreet;
  }

  public String getLocalNumber() {
    return localNumber;
  }

  public void setLocalNumber(String localNumber) {
    this.localNumber = localNumber;
  }

  public String getLocalComplement() {
    return localComplement;
  }

  public void setLocalComplement(String localComplement) {
    this.localComplement = localComplement;
  }

  public String getLocalCity() {
    return localCity;
  }

  public void setLocalCity(String localCity) {
    this.localCity = localCity;
  }

  public String getLocalState() {
    return localState;
  }

  public void setLocalState(String localState) {
    this.localState = localState;
  }

  public String getLocalNeighborhood() {
    return localNeighborhood;
  }

  public void setLocalNeighborhood(String localNeighborhood) {
    this.localNeighborhood = localNeighborhood;
  }

}
