package com.werner.bl.input.generated;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Graph
 */
public class Graph {
  @SerializedName("nodes")
  private List<GraphNodes> nodes = null;

  @SerializedName("edges")
  private List<GraphEdges> edges = null;

  public Graph nodes(List<GraphNodes> nodes) {
    this.nodes = nodes;
    return this;
  }

  public Graph addNodesItem(GraphNodes nodesItem) {
    if (this.nodes == null) {
      this.nodes = new ArrayList<GraphNodes>();
    }
    this.nodes.add(nodesItem);
    return this;
  }

   /**
   * Get nodes
   * @return nodes
  **/
  public List<GraphNodes> getNodes() {
    return nodes;
  }

  public void setNodes(List<GraphNodes> nodes) {
    this.nodes = nodes;
  }

  public Graph edges(List<GraphEdges> edges) {
    this.edges = edges;
    return this;
  }

  public Graph addEdgesItem(GraphEdges edgesItem) {
    if (this.edges == null) {
      this.edges = new ArrayList<GraphEdges>();
    }
    this.edges.add(edgesItem);
    return this;
  }

   /**
   * Get edges
   * @return edges
  **/
  public List<GraphEdges> getEdges() {
    return edges;
  }

  public void setEdges(List<GraphEdges> edges) {
    this.edges = edges;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Graph graph = (Graph) o;
    return Objects.equals(this.nodes, graph.nodes) &&
        Objects.equals(this.edges, graph.edges);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodes, edges);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Graph {\n");
    
    sb.append("    nodes: ").append(toIndentedString(nodes)).append("\n");
    sb.append("    edges: ").append(toIndentedString(edges)).append("\n");
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

