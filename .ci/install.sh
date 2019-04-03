#!/bin/bash

set -e
set -x
<<<<<<< HEAD
#ls -A | grep -v src | xargs rm -r || :
if [[ "$(uname -s)" == 'Darwin' ]]; then
    brew update || brew update
    brew install cmake || true
fi
pip install conan --upgrade
pip install conan_package_tools bincrafters_package_tools
conan user
=======
if [[ "$(uname -s)" == 'Darwin' ]]; then
    brew install cmake || :
    brew install python3 || :
fi
pip3 install conan --upgrade
>>>>>>> 99ec824b8c72c7d7f4b1a8272c88c2d8ab30a317
