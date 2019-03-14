from conans import ConanFile,tools

class Solo5Conan(ConanFile):
    settings= "compiler","arch","build_type","os"
    name = "solo5"
    version = "0.4.1"
    url = "https://github.com/includeos/solo5.git"
    description = "A sandboxed execution environment for unikernels. Linux only for now."
    license = "ISC"

    def source(self):
        repo = tools.Git(folder = self.name)
        repo.clone(self.url, branch="ssp-fix")

    def build(self):
        self.run("CC=gcc ./configure.sh", cwd=self.name)
        self.run("make", cwd=self.name)

    def package(self):
        #grab evenrything just so its a reausable redistributable recipe
        self.copy("*.h", dst="include/solo5", src=self.name + "/include/solo5")
        self.copy("*.o", dst="lib", src=self.name + "/bindings/hvt/")
        self.copy("solo5-hvt", dst="bin", src= self.name + "/tenders/hvt")
        self.copy("solo5-hvt-configure", dst="bin", src= self.name + "/tenders/hvt")

    def deploy(self):
        self.copy("*", dst="lib",src="lib")
        self.copy("*", dst="bin",src="bin")
        self.copy("*", dst="include", src="include")
