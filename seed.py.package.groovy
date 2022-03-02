//github server setting
String github_token_credential = "git-token-credentials"
String github_host = "github.com"

//central pipeline repository
String pipeline_github_repository = "wuqunfei/jenkins_ai_pipelines"
String pipeline_jenkins_file = "Jenkinsfile.py.package.groovy"

//package source code
String source_code_repository_url = "https://github.com/wuqunfei/ocr_package"
String source_code_branch = "main"

//nexus setting
String nexus_py_repository_url = "http://localhost:8081/repository/pyrepo/"
String nexus_credential= 'nexus_credential'

pipelineJob("ocr-package-builder") {

    parameters {

        stringParam("source_code_repository_url", source_code_repository_url, "Package Source Code HTTP URL")
        stringParam("source_code_branch", source_code_branch, "Package Source Code Branch, default main")
        stringParam('github_token_credential', github_token_credential, 'Github token credential id')

        stringParam("pipeline_github_repository", pipeline_github_repository, "pipeline github project name")
        stringParam("pipeline_jenkins_file", pipeline_jenkins_file, 'pipeline file')

        stringParam("nexus_py_repository_url", nexus_py_repository_url,'http nexus pypi repository')
        stringParam('nexus_credential', nexus_credential, 'neus nexus_credential id')
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github(pipeline_github_repository, "https", github_host)
                        credentials(github_token_credential)
                    }
                }
            }
            scriptPath(pipeline_jenkins_file)
        }
    }
}


