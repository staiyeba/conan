#!/usr/bin/env groovy

def build(versions, architectures, build_types) {
  versions = "${versions}".replaceAll("\\s", "").split(',')
  architectures = "${architectures}".replaceAll("\\s", "").split(',')
  build_types = "${build_types}".replaceAll("\\s", "").split(',')

  def builds = [:]

  for (ver in versions) {
    for (arch in architectures) {
      for (build in build_types) {
        String buildName = "${build}-${arch}-${ver}"

        builds[buildName] = {
          node('conan_pipe_worker') {
            stage(buildName) {
              echo buildName
            }
          }
        }

      }
    }
  }
  parallel builds
}

def create_binutils_build_commands(version, profiles, target_oss, target_architectures, build_types, conanfile_path) {
  // clean the input parameters
  profiles = "${profiles}".replaceAll("\\s", "").split(',')
  target_oss = "${target_oss}".replaceAll("\\s", "").split(',')
  target_architectures = "${target_architectures}".replaceAll("\\s", "").split(',')
  build_types = "${build_types}".replaceAll("\\s", "").split(',')

  // Loop to create all build tasks
  def builds = [:]

  for (prof in profiles) {
    for (t_os in target_oss) {
      for (t_arch in target_architectures) {
        for (b_type in build_types) {
          String buildName = "${prof}-${b_type}-${t_os}-${t_arch}"

          builds[buildName] = """
            echo "buildName: ${buildName}"
            echo "conanfile path: ${conanfile_path}"
            cat "${conanfile_path}"
          """
        }
      }
    }
  }

  return builds
}

def conanfile_path(jenkinsfile_path, version) {
  def regexSuffix = ~/\/Jenkinsfile$/
  def path = "${jenkinsfile_path}" - regexSuffix
  def conanfile = "${path}/${version}/conanfile.py"
  return conanfile
}


def upload(target) {
   echo "Uploading to: ${target}"
}

return this;
