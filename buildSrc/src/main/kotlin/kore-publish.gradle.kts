/*
 * Copyright 2026 Davils
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.davils.buildsrc.Project

plugins {
    com.davils.kreate
    com.vanniktech.maven.publish
}

kreate {
    project {
        publish {
            enabled = false
            inceptionYear = Project.Identity.INCEPTION_YEAR
            website = Project.Organization.WEBSITE_URL

            pom {
                issueManagement {
                    system = Project.IssueManagement.SYSTEM
                    url = Project.IssueManagement.URL
                }

                ciManagement {
                    system = Project.VersionControl.CI_SYSTEM
                    url = Project.VersionControl.CI_URL
                }

                licenses {
                    license {
                        name = Project.Legal.LICENSE_NAME
                        url = Project.Legal.LICENSE_URL
                        distribution = Project.Legal.LICENSE_DISTRIBUTION
                    }
                }

                developers {
                    developer {
                        id = Project.Organization.NAME.lowercase()
                        name = Project.Organization.NAME
                        email = Project.Organization.EMAIL
                        organization = Project.Organization.NAME
                        timezone = Project.Organization.TIMEZONE
                    }
                }

                scm {
                    url = Project.VersionControl.SCM_URL
                    connection = Project.VersionControl.SCM_CONNECTION
                    developerConnection = Project.VersionControl.SCM_DEVELOPER_CONNECTION
                }
            }

            repositories {
                gitlab {
                    enabled = false
                }

                mavenCentral {
                    enabled = true
                    automaticRelease = true
                    signPublications = true
                }
            }
        }
    }
}
