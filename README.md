# Conan dependency building
Contains all configuration files for building IncludeOS dependencies.
These are the main categories of build jobs:
 - Dependencies: 3rd party external dependencies
 - Tools: Internal IncludeOS tools

# Creating/adding new build jobs
To create a new build job:
Place a `Jenkinsfile` and a `conanfile.py` in the correct directory structure.
The Jenkinsfile should contain:
- Parameters that the dependency can be built with.
	- **IMPORTANT:** Make the choice parameter always have an empty item as the first choice. This is needed when initializing the jobs.
- A stage that builds and calls the external

# Workflow
1. Root Jenkinsfile: JobDSL to create 1 **Unique** build job pr dependency.
2. Each dependency needs to have a Jenkinsfile and a conanfile.py
3. Root Jenkinsfile: Scans the includeos/conan repo for all Jenkinsfiles excluding itself. 
