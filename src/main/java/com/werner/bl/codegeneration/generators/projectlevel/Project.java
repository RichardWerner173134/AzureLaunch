package com.werner.bl.codegeneration.generators.projectlevel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Project {
    private String projectRoot;

    private String artifactId;

    private String groupId;
}
