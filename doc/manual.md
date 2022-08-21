
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

Open a training configuration from *File* > *Open* > *Training*. Choose a training configuration file. An example file is shipped with the release package, and their extension is `*.training.json`.

You should see something like this:

<img src="https://raw.githubusercontent.com/hnen/NumberClassifier/master/doc/img/training.png" width="350">

You can tweak the parameters, and after that start the training with "Train" button. After training is complete, the network is written to the output file, and you can try it out yourself (see next chapter.)

After each training, the app will write results to `train-stats.csv`.

### Drawing

You can open a trained neural network from *File* > *Open* > *Neural network*. Neural network files have file format `*.neuralnetwork.json`. After opening the file, you should see something like this:

<img src="https://raw.githubusercontent.com/hnen/NumberClassifier/master/doc/img/drawing1.png" width="350">

You can draw the image with left mouse button. You can erase drawing with right mouse button (still TODO.) The neural network evaluates the drawing in real-time like this:

<img src="https://raw.githubusercontent.com/hnen/NumberClassifier/master/doc/img/drawing2.png" width="350">


