// The folderName is used below when giving name to all the jobs. This to ensure that they end up in the correct folder.
def folderName = 'IncludeOS Github Organization/conan'

// Name/label of the worker that should be used, this name is also set in the individual build jobs Jenkinsfile
def worker_node = 'buildpkg-ubuntu1804'

// Settings for conan
def artifactory_name = "kristianj"
def artifactory_repo = "includeos-develop"
def conan_user = 'includeos'
def conan_channel = 'test'
def conan_home = '/home/ubuntu'

// Create a folder to keep all the jobs that are created in. Also create 3 views based on what type of build job it is.
stage('Create folders') {
  node() {
    jobDsl scriptText: """
      folder("${folderName}") {
        displayName('Conan')
        description('Build jobs for all IncludOS dependencies built with Conan')
        views {
          listView('includeos') {
            jobs {
              regex('includeos-.+')
            }
            columns {
              status()
              weather()
              name()
              lastSuccess()
              lastFailure()
              lastDuration()
              buildButton()
            }
          }
          listView('dependencies') {
            jobs {
              regex('dependencies-.+')
            }
            columns {
              status()
              weather()
              name()
              lastSuccess()
              lastFailure()
              lastDuration()
              buildButton()
            }
          }
          listView('libraries') {
            jobs {
              regex('libraries-.+')
            }
            columns {
              status()
              weather()
              name()
              lastSuccess()
              lastFailure()
              lastDuration()
              buildButton()
            }
          }
          listView('tools') {
            jobs {
              regex('tools-.+')
            }
            columns {
              status()
              weather()
              name()
              lastSuccess()
              lastFailure()
              lastDuration()
              buildButton()
            }
          }
        }
      }
    """
  }
}

stage('Create jobs') {
  node("${worker_node}") {
    // Checkout the repo to get all the Jenkinsfiles and create build jobs
    checkout scm
    // Find the url of the repo, and use this when generating the pipelinejobs
    def git_url = sh(returnStdout: true, script: 'git config remote.origin.url').trim()
    // Find all Jenkinsfiles in the whole catalog
    def jenkinsfiles = findFiles(glob: '**/Jenkinsfile')
    // Regex used to remove the fileName and only have the paths
    def regexSuffix = ~/\/Jenkinsfile$/
    // All job creation will be run in parallel
    def jobs = [:]

    for (def file: jenkinsfiles) {
      if ("${file.path}" == "Jenkinsfile") {
        echo "Skipping root Jenkinsfile"
        continue
      }
      // Remove file name, modify path and append folder name
      // final name should be of the format: <folderName>/<path>-<name>
      String name = "${file.path}" - regexSuffix
      name = name.replace("/", "-")
      name = "${folderName}/${name}"
      def path = "${file.path}" // trick to avoid overwriting when using parallel

      // Add the jobDsl script to the jobs list
      jobs["${name}"] = {
        jobDsl scriptText: """
          pipelineJob("${name}") {
            description("Pipeline for ${name}")
            environmentVariables {
              env('SCRIPT_PATH', "${path}")
              env('CONAN_USER', "${conan_user}")
              env('CONAN_CHANNEL', "${conan_channel}")
              env('CONAN_USER_HOME', "${conan_home}")
            }
            definition {
              cpsScm {
                lightweight(true)
                scm {
                  git {
                    remote {
                      url("${git_url}")
                      credentials('jenkins-includeos-user-pass')
                    }
                    branch('master')
                    scriptPath("${path}")
                    extensions {
                      cleanBeforeCheckout()
                    }
                  }
                }
              }
            }
          }
        """
        // Call build to initiate the job properly, this causes the pipeline to run once. This makes sure that the parameters are available when the job is to be run for real. It is called with default parameters, and when version='' the build job is not actually run.
        build "${name}"
      }
    }
    // Run all the loaded jobs in parallel. At this point they will have to be manually approved in the "In-process Script approval" console in Jenkins.
    parallel jobs
  }
}
