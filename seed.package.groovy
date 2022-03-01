String github_token = "git-token-credentials"
String github_host = "github.com"

String pipeline_github_repository = "wuqunfei/jenkins_ai_pipelines"
String pipeline_jenkins_file = "Jenkinsfile.package.groovy"

String source_code_repository_url = "https://github.com/wuqunfei/ocr_package"
String source_code_branch = "main"

String nexus_py_repository_url = "http://localhost:8081/repository/pyrepo/"

String nexus_user = "admin"
String nexus_pwd = "admin"

pipelineJob("ocr-package-seed") {
    parameters {
        stringParam("source_code_repository_url", source_code_repository_url, "Source Code HTTP URL")
        stringParam("source_code_branch", source_code_branch, "Source Code Branch, default master")
        stringParam('github_token', github_token, 'Github token credential id')

        stringParam("pipeline_github_repository", pipeline_github_repository, "pipeline github project name")
        stringParam("pipeline_jenkins_file", pipeline_jenkins_file, 'pipeline file')

        stringParam("nexus_py_repository_url", nexus_py_repository_url,'http nexus pypi repository')
    }
    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        github(pipeline_github_repository, "https", github_host)
                        credentials(github_token)
                    }
                }
            }
            scriptPath(pipeline_jenkins_file)
        }
    }
}


