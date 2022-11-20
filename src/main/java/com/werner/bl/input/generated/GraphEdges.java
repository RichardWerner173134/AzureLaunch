package com.werner.bl.input.generated;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * GraphEdges
 */
public class GraphEdges {
  @SerializedName("node1")
  private String node1 = null;

  @SerializedName("node2")
  private String node2 = null;

  @SerializedName("type")
  private String type = null;

  public GraphEdges node1(String node1) {
    this.node1 = node1;
    return this;
  }

   /**
   * Get node1
   * @return node1
  **/
  public String getNode1() {
    return node1;
  }

  public void setNode1(String node1) {
    this.node1 = node1;
  }

  public GraphEdges node2(String node2) {
    this.node2 = node2;
    return this;
  }

   /**
   * Get node2
   * @return node2
  **/
  public String getNode2() {
    return node2;
  }

  public void setNode2(String node2) {
    this.node2 = node2;
  }

  public GraphEdges type(String type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GraphEdges graphEdges = (GraphEdges) o;
    return Objects.equals(this.node1, graphEdges.node1) &&
        Objects.equals(this.node2, graphEdges.node2) &&
        Objects.equals(this.type, graphEdges.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(node1, node2, type);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GraphEdges {\n");
    
    sb.append("    node1: ").append(toIndentedString(node1)).append("\n");
    sb.append("    node2: ").append(toIndentedString(node2)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
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

