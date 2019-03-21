#!/usr/bin/env groovy

def upload_package_command(version, conanfile_path, conan_user, conan_channel, upload_channel, conan_remote) {

  String buildCmd = "conan upload --all ${conanfile_path}@${conan_user}/"

  if (upload_channel.length() > 0) {
    if (conanfile_path.contains('binutils')) {
      buildCmd += "toolchain"
    }
    else {
      buildCmd += "${upload_channel}"
    }
  }
  else {
    buildCmd += "${conan_channel}"
  }

  buildCmd += " -r ${conan_remote}"

  builds = "${buildCmd}"
  return builds
}

// FUNCTION: not in use atm
// only use when copying package from test repo to official includeos repo
def copy_package_official(version, conanfile_path, conan_user, conan_channel, upload_channel) {
  String buildCmd = "conan copy --all -p ${conanfile_path} ${conanfile_path}@${conan_user}/"

  if (upload_channel.length() > 0) {
    buildCmd += "${upload_channel}"
  }
  else {
    buildCmd += "${conan_channel}"
  }
  // TODO::
  // name of official repo in < >(not test or test-package)
  // default repo: test (atm)
  // default channel: test (atm)
  // official repo in use atm : test-packages
  buildCmd += " -r ${conan_user}/<includeos-official>"

  builds = "${buildCmd}"
  return builds

}

def conanfile_path(jenkinsfile_path, version) {
  def regexSuffix = ~/\/Jenkinsfile$/
  def path = "${jenkinsfile_path}" - regexSuffix
  def conanfile = "${path}/${version}"
  return conanfile
}

def create_dependencies_build_commands(version, profiles, target_oss, target_architectures, build_types, conanfile_path, conan_user, conan_channel, upload_channel) {
  // clean the input parameters
  profiles = "${profiles}".replaceAll("\\s", "").split(',')
  target_oss = "${target_oss}".replaceAll("\\s", "").split(',')
  target_architectures = "${target_architectures}".replaceAll("\\s", "").split(',')
  build_types = "${build_types}".replaceAll("\\s", "").split(',')
  file_path = "${conanfile_path}"
  upload_channel = "${upload_channel}"
  // Loop to create all build tasks
  def builds = [:]

  for (prof in profiles) {
    for (t_os in target_oss) {
      for (t_arch in target_architectures) {
        for (b_type in build_types) {
          String buildName = "${prof}-${b_type}-${t_os}"
          String buildCmd = "conan create ${conanfile_path} -pr ${prof} ${conan_user}/"

          if (file_path.contains('binutils')) {
            buildCmd += "toolchain"
          }
          else if (upload_channel.length() > 0) {
            buildCmd += "${upload_channel}"
          }
          else {
            buildCmd += "${conan_channel}"
          }

          if (b_type.length() > 0) {
            buildCmd += " -s build_type=${b_type}"
          }
          if (t_os.length() > 0) {
            buildCmd += " -s os=${t_os}"
          }
          if (t_arch.length() > 0) {
            buildCmd += " -s arch=${t_arch}"
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

return this;
