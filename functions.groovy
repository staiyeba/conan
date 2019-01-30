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

def upload(target) {
   echo "Uploading to: ${target}"
}

return this;
