#!/usr/bin/env groovy

def create_dependencies_build_commands(version, profiles, target_oss, build_types, conanfile_path, conan_user, conan_channel) {
  // clean the input parameters
  profiles = "${profiles}".replaceAll("\\s", "").split(',')
  target_oss = "${target_oss}".replaceAll("\\s", "").split(',')
  build_types = "${build_types}".replaceAll("\\s", "").split(',')

  // Loop to create all build tasks
  def builds = [:]

  for (prof in profiles) {
    for (t_os in target_oss) {
        for (b_type in build_types) {
          String buildName = "${prof}-${b_type}-${t_os}"
          String buildCmd = "conan create ${conanfile_path} -pr ${prof} ${conan_user}/${conan_channel}"

          if (b_type.length() > 0) {
            buildCmd += " -s build_type=${b_type}"
          }
          if (t_os.length() > 0) {
            buildCmd += " -s os=${t_os}"
          }
          builds[buildName] = """
            ${buildCmd}
          """
        }
    }
  }
  return builds
}

def create_binutils_build_commands(version, profiles, target_oss, target_architectures, build_types, conanfile_path, conan_user, conan_channel) {
  // clean the input parameters
  profiles = "${profiles}".replaceAll("\\s", "").split(',')
  target_oss = "${target_oss}".replaceAll("\\s", "").split(',')
  target_architectures = "${target_architectures}".replaceAll("\\s", "").split(',')
  build_types = "${build_types}".replaceAll("\\s", "").split(',')

  // Loop to create all build tasks
  def builds = [:]
  // profiles should get profiles labelled toolchain for binutils
  for (prof in profiles) {
    for (t_os in target_oss) {
      for (t_arch in target_architectures) {
        for (b_type in build_types) {
          String buildName = "${prof}-${b_type}-${t_os}-${t_arch}"
          String buildCmd = "conan create ${conanfile_path} -pr ${prof} ${conan_user}/${conan_channel}"

          if (b_type.length() > 0) {
            buildCmd += " -s build_type=${b_type}"
          }
          if (t_os.length() > 0) {
            buildCmd += " -s os=${t_os}"
          }
          builds[buildName] = """
            ${buildCmd}
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
