package com.werner.bl.input.generated;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * parsed file result
 */
public class ParseFileRequest {
  @SerializedName("azAccount")
  private AzAccount azAccount = null;

  @SerializedName("graph")
  private Graph graph = null;

  public ParseFileRequest azAccount(AzAccount azAccount) {
    this.azAccount = azAccount;
    return this;
  }

   /**
   * Get azAccount
   * @return azAccount
  **/
  public AzAccount getAzAccount() {
    return azAccount;
  }

  public void setAzAccount(AzAccount azAccount) {
    this.azAccount = azAccount;
  }

  public ParseFileRequest graph(Graph graph) {
    this.graph = graph;
    return this;
  }

   /**
   * Get graph
   * @return graph
  **/
  public Graph getGraph() {
    return graph;
  }

  public void setGraph(Graph graph) {
    this.graph = graph;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ParseFileRequest parseFileRequest = (ParseFileRequest) o;
    return Objects.equals(this.azAccount, parseFileRequest.azAccount) &&
        Objects.equals(this.graph, parseFileRequest.graph);
  }

  @Override
  public int hashCode() {
    return Objects.hash(azAccount, graph);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ParseFileRequest {\n");
    
    sb.append("    azAccount: ").append(toIndentedString(azAccount)).append("\n");
    sb.append("    graph: ").append(toIndentedString(graph)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}

