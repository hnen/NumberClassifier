
# Test Document

## Unit testing

Auto-generated unit testing code coverage report can be downloaded [here](https://nightly.link/hnen/NumberClassifier/workflows/gradle/master/codecov-report.zip).

Unit testing makes sure smaller parts of the algorithms work correctly. Brief description of tests:

### `ImageSetTest`
- Test that MNIST file format(that is used for the training data) is parsed correctly using a simple hand-made example.
- Test that the images are transformed into `TrainingExample`s correctly. This is needed to input and test the images with neural network.

### `FeedForwardNeuralNetworkTest`
 - Test that the class doesn't accept invalid input.
 - Test with trivial exampe, that feedforward algorithm works correctly. Feedforward is vital for everything to work correctly so this is very important.
 - Test that parameter gradients are calculated correctly. 'Correctness' was slightly harder to define in this case, but unit tests do some sanity checks, like that the gradient signs are towards right direction, and that applying gradients reduce neural network cost function.
 - Test that full training epoch decreases the value of the function. That's what the training, by definition, aims to do.

### `FeedForwardNeuralNetworkParametersTest`
 - Test that primitive operations like addition and weight matrix generation work correctly. This is pretty trivial to test.
 - Test for invalid input.

### `TrainConfigTest`
 - Test that JSON data is parsed properly.

### `NeuralNetworkTrainerTest`
 - Test that trainer is capable of training a trivial classifier network with 100% accuracy.
 - TODO: Test that smaller parts of trainer behaves like it's defined to do, for example: feedforward is called right number of times, mini batches are generated correctly, etc.

## Training the neural network

Training network takes following input:

Hyper Parameters:
- `layers`: Neural network topology. Number of layers and number of neurons on each layer, as an integer array.    
- `activation`: Activation function. Two activation functions were implemented, Sigmoid functions("sigmoid") and Rectified Linear Unit ("relu")
- `learningRate`: Multiplier for each gradient descent step. Bigger value trains the network faster, but is numerically less stable.
- `initWeights`: Initial range for neuron connection weights, as 2-element array. Weights are uniformly randomized in this range.
- `initBiases`: Initial value for neuron initial biases.
- `epochs`: Number of epoch during training.
- `miniBatchSize`: Size of the batch used on every epoch.

File parameters:
 - `trainingData`: File containing the number images for training in MNIST format.
 - `trainingLabels`: File containing the corresponding number labels for trainign in MNIST format.
 - `testData`: File containing the number images for testing accuray in MNIST format.
 - `testLabels`: File containing the number labels for testing accuracy in MNIST format.
 - `outFile`: File to output the trained network to in JSON format.

To test the training, input the parameters to `training-config.json` and run `App.main` entry point of the codebase. [NOTE: This is subject to change.]

After training the network, the trainer measures the accuracy metric for the network by evaluating its results for every test example. Accuracy metric is number of correctly guessed test examples divided by total number of test examples. The accuracy value is quite good metric that defines whether the algorithm works 'correctly'. However more complicated question is, how good the accuracy metric should  expected to be. According to MNIST, neural networks have been capable of achieving 95.3% - 99.65% accuracy rate, so if the training works correctly, 95% accuracy should be achievable with relatively low effort.

With some ad-hoc hand tweaking of values, 96.95% accuracy was achieved with following configuration:
```
{
    "trainingData": "data/train-images.idx3-ubyte",
    "trainingLabels": "data/train-labels.idx1-ubyte",
    "testData": "data/t10k-images.idx3-ubyte",
    "testLabels": "data/t10k-labels.idx1-ubyte",

    "outFile": "nn.json",

    "layers": [ 784, 32, 32, 10 ],
    
    "activation": "relu",
    "learningRate": 0.05,
    "initWeights": [-0.1, 0.1],
    "initBiases": 0.01,

    "epochs": 50000,
    "miniBatchSize": 15
}
```

## Evaluating hand-written numbers

Main motivation for training the network is to being able to apply it to input that its never seen before. For this, an interactive app was built where user can draw a number and the network guesses the number.

Correctness for this algorithm is slightly more subjective. One definition could be, that it should be able to identify anyone's hand-written numbers with same accuracy rate than against the MNIST test examples.

Replicating this test can be done by running the interactive app from `InteractiveApp.main` entry point.

First subjective test can be done by simply doodling few numbers to the app. If the app is struggling to recognize some numbers, the algorithm needs improvements.

TODO: More comprehensive testing of this method.

## References

 - http://yann.lecun.com/exdb/mnist/
