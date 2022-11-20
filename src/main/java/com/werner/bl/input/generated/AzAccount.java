package com.werner.bl.input.generated;

import com.google.gson.annotations.SerializedName;
import java.util.Objects;

/**
 * AzAccount
 */
public class AzAccount {
  @SerializedName("subscription")
  private String subscription = null;

  @SerializedName("resourceGroup")
  private String resourceGroup = null;

  public AzAccount subscription(String subscription) {
    this.subscription = subscription;
    return this;
  }

   /**
   * Get subscription
   * @return subscription
  **/
  public String getSubscription() {
    return subscription;
  }

  public void setSubscription(String subscription) {
    this.subscription = subscription;
  }

  public AzAccount resourceGroup(String resourceGroup) {
    this.resourceGroup = resourceGroup;
    return this;
  }

   /**
   * Get resourceGroup
   * @return resourceGroup
  **/
  public String getResourceGroup() {
    return resourceGroup;
  }

  public void setResourceGroup(String resourceGroup) {
    this.resourceGroup = resourceGroup;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AzAccount azAccount = (AzAccount) o;
    return Objects.equals(this.subscription, azAccount.subscription) &&
        Objects.equals(this.resourceGroup, azAccount.resourceGroup);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscription, resourceGroup);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AzAccount {\n");
    
    sb.append("    subscription: ").append(toIndentedString(subscription)).append("\n");
    sb.append("    resourceGroup: ").append(toIndentedString(resourceGroup)).append("\n");
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

