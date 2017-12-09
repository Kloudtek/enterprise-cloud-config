@Library('kloudtek-pipelinelib') _

mavenRelease releaseBranch: "master", ciCmd:"-Dmaven.test.failure.ignore -P release clean deploy", releaseCmd: "-P release clean deploy", dirs= ['client','server']
