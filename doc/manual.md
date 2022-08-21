
# NumberClassifier Manual

## Running the app

### Run from release

You Java Runtime Environment must support class files version 60. This is shipped e.g. with JDK 16.

Download the release package from [this link](https://nightly.link/hnen/NumberClassifier/workflows/gradle/master/NumberClassifier-release.zip). Run `java -jar NumberClassifier.jar`

### Run from sources

Download JDK 16 to your computer. Make `JAVA_HOME` environment variable point to the JDK location. Run `./gradlew build run` in `NumberClassifier` directory of the repository.

## Using the app

You can both train neural networks and evaluate numbers with trained networks in the app.

### Training

Open a training configuration in 'File > Open > Training`. Choose a training configuration file. An example file is shipped with the release package, and their extension is `*.training.json`.




