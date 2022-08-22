
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

While running the training, program draws a real-time chart on how the loss function evolves during the training. Smaller value means more accurate outcome. The loss function is evaluated against a small subset of the testing dataset. When running subsequent trainings, a new series will drawn on top of the previous ones, making it possible to compare performance of different training runs. The testing dataset is always kept the same, to make graphs comparable.

<img src="https://raw.githubusercontent.com/hnen/NumberClassifier/master/doc/img/traininggraph.png" width="350">

In the example above, red run had the best outcome. Orange performed poorly, and didn't improve practically much after epoch 3000. Green did ok, but fluctuated a lot - decreasing the learning rate could make the learning more stable.

After each training, the app will write results to `train-stats.csv`. You can use the table to recall best performed hyper parameters and try to replicate the results.

#### Note about parameters

You can tweak some hyper parameters while the training is running - mainly the ones under "training strategy". While this can be useful to observe effects of hyper parameter in real-time(this was an accidental "feature"), there are few gotchas that may result in incorrect results or crashes:
 - Don't reduce number of passes during training (i.e. don't decrease epoch or learning rate array size)
 - Only the latest active parameters are written in train-stats.csv - changes during training are not kept track of.

### Drawing

You can open a trained neural network from *File* > *Open* > *Neural network*. Neural network files have file format `*.neuralnetwork.json`. After opening the file, you should see something like this:

<img src="https://raw.githubusercontent.com/hnen/NumberClassifier/master/doc/img/drawing1.png" width="350">

You can draw the image with left mouse button. You can erase drawing with right mouse button. The neural network evaluates the drawing in real-time like this:

<img src="https://raw.githubusercontent.com/hnen/NumberClassifier/master/doc/img/drawing2.png" width="350">


