
# Test Document

## Unit testing code coverage.

Generated unit testing code coverage report can be downloaded here.

## Initial testing observations

Following hyper parameters were configurable:

- `layers`: Neural network topology. Number of layers and number of neurons on each layer, as an integer array.    
- `activation`: Activation function. Two activation functions were implemented, Sigmoid functions("sigmoid") and Rectified Linear Unit ("relu")
- `learningRate`: Multiplier for each gradient descent step. Bigger value trains the network faster, but is numerically less stable.
- `initWeights`: Initial range for neuron connection weights, as 2-element array. 
- `initBiases`: Initial value for neuron initial biases.
- `epochs`: Number of epoch during training.
- `miniBatchSize`: Size of the batch used on every epoch.

For every run, the network was trained with hyper parameters and accuracy was measured against the MNIST test examples. Accuracy was outputted as percentage on how many test examples the model guessed correctly. At first I hand tuned the parameters in ad-hoc fashion and observed the effect on accuracy metric. Playing around for a little while, I got best results with following hyper parameters, resulting in approx. 95% accuracy:
```
"layers": [ 784, 32, 32, 10 ],

"activation": "relu",
"learningRate": 0.025,
"initWeights": [-0.1, 0.1],
"initBiases": 0.01,

"epochs": 20000,
"miniBatchSize": 10
```

Initial observations:
 - With ReLU activation function, hyper parameters have to be tweaked bit more carefully, since parameters can easily explode and result into 0 derivatives and incorrect result. Initial weights especially have to be small enough. Sigmoid function seems to behave numerically more stable with wider range of parameters.
 - However with right parameters, ReLU seems to converge towards good accuracy faster.
 - More neurons doesn't necessarily result into better accuracy.
 - More neurons tend to require lower learning rate.
 - Likewise, increasing batch size doesn't improve the accuracy after certain limit, and the value has one of the biggest impact into training speed, so it's good idea to tweak the parameter to as low numebr as possible.



