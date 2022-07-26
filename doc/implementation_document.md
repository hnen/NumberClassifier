# Implementation Document

## Program structure

The program is currently divided into following modules:
 - `data`: Code related to handling of datasets and encoding/decoding the data for the neural network.
 - `gui`: The user interface
 - `neuralnetwork`: Neural network data structure. Some interesting classes include:
   - `FeedForwardNeuralNetwork`: Data structure for the state of neural network. Contains the feedforward algorithm, i.e. the algorithm that "evaluates the result", and backpropagation algorithm that is used for training the network.
   - `FeedForwardNeuralNetworkParameters`: Data structure that defines the neural network. This is output from the training algorithm.
 - `serialization`: Code related to JSON serialization
 - `train`: The neural network training algorithm and neural network accuracy testing.
 - `stats`: Code related to gathering performance statistiscs of the training runs.

## Overview of the implementation

The program implements loading and processing an image dataset (MNIST file format is supported), running a neural network training algorithm, and classifying a user drawn image with the trained neural network.

### Image processing

Images are centered and scaled to fit the image bounds. This reduces errors especially with user drawn images, that vary easily in size and positioning.

### Training the neural network

Training algorithm optimizes parameters with a stochastic gradient descent using RMSprop strategy. Network gradients are calculated with backpropagation algorithm. Training rate schedule is configurable.

## Implementation details, time and space complexities

### Data structure space complexity

Neural network parameters consists of neurons and their connections. The network has `N` layers and `n_i` neurons on layer `i`. Hence, number of neurons is `n_1 + n_2 + ... + n_N` Each neuron on layer `n` is connected to each neuron on layer `n + 1`, hence the number of connections is `n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N`.

The network stores a bias value for each neuron and a weight value for each connection (see `FeedForwardNeuralNetworkParameters`), hence the space complexity of the neural network data structure is 
```
  O(number_of_neurons + number_of_weights)
= O((n_1 + n_2 + ... + n_N) + (n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N)) 
= O(n_1 * (n_2 + 1) + n_2 * (n_3 + 1) + ... + n_(N-1) * (n_N + 1) + n_N)) 
= O(n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N))
```

### Feedforward algorithm

Feedforward algorithm takes the neural network parameters(biases and weights) as an input, and `n_1` activation values `a_11, a_12, ..., a_1(n_1)`. The output of the algorithm is activation values(i.e. the output) of each neuron, `a_ij`, and input values of each neuron `k_ij`. The activation function `f`, which maps neuron input to its output could be also considered as input.

When training the network, all activations and inputs are needed. When just evaluating result of the network, only last layer activations `a_N1, a_N2, ..., a_N(n_N)` is relevant output and no inputs are needed.

Java implementation of the backpropagation is in `FeedforwardNeuralNetwork.feedForward`

Pseudocode of the feedforward is below. `b_li` is bias for neuron `i` on layer `l` and `w_l_ij` is weight for connection from neuron `i` on layer `l` to neuron `j` on layer `l+1`.
```
--- FeedForward ---
for l in 1 -> N - 1:
    for i = 1 -> n_(l-1):
        I = b_li;
        for j 1 -> n_l:
            I = I + a_lj * w_l_ij;

        k_(l+1)i = I;
        a_(l+1)i = f(I);
```

This algorithm stores activation values `a_li` for each neuron, making the space complexity `O(n)` where `n` is total number of neurons. 

The algorithm has three nested loops from which it's trivial to derive time complexity of `O(n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N)`, or `O(m)` where `m` is total number of connections.

### BackPropagate algorithm

Backpropagate is used in the training algorithm.

Backpropagation algorithm takes the neural network parameters(biases and weights), `n_1` sample input values `i_1, i_2, ..., i_(n_1)` and `n_N` sample output values `o_1, o_2, ..., o_(n_N)` as an input. The output is a scalar value for each weight and bias in the network, that represents each parameters' direction and magnitude that takes the feedforward output closer to the desired result, i.e. calculates gradient of the cost function. 

Java implementation of the backpropagation is in `FeedforwardNeuralNetwork.calculateCostGradient`

In this pseudocode neural network parameters uses same names as previously, and the gradient of biases are denoted with `Db_li` and weights `Dw_l_ij`. `df` is derivative of the activation function `f`.
```
--- BackPropagate ---

Execute FeedForward with the sample input values. 

for i in 1 -> n_N:
    e_Ni = (a_Ni - o_i) * df(k_Ni);

for l (N-1) -> 1:
    for i 1 -> n_l:
        for j 1 -> n_(l+1):
            e_li += e_(l+1)j * w_l_ij;
        e_li = e_li * df(k_li);

for l 2 -> N:
    for i 1 -> n_l:
        Db_(l-1)i = e_li;

for l 1 -> N - 1:
    for i 0 -> n_l:
        for j -> n_(l+1):
            Dw_l_ij = e(l+1)j * a_li

```

The algorithm stores intermediate errors to `e`, which has value for each neuron in the network. Thus, the space complexity of the algorithm is linear compared to number of neurons in the network, i.e. `O(n_1 + n_2 + ... + n_N)`

The algorithm iterates each parameter of the network twice, so the time complexity of the algorithm is linear compared to parameters of the network, or specified as a relationship to the number of neurons on each layer, `O(n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N)`

## Training algorithm

Let's define few utility functions for neural network parameters:

```
--- Add ---
Input: x, y: neural network parameters
Output: result: neural network parameters
for l 1 -> N:
    for i 1 -> n_l:
        result_b_li = x_b_li + y_b_li
for l 1 -> (N-1):
    for i 1 -> n_l:
        for j -> n_(l+1):
            result_l_ij = x_w_l_ij + y_w_l_ij

Similarly we will have following operations that perform mathematical operations on each parameter of the neural network. They may take also scalar as second parameter. For Square and SquareRoot there is no other parameters, but the function is applied on each parameter. The time complexity is identical in each case:

Mul, Divide, Add, Square and SquareRoot
```

Training incrementally modifies the parameters by its gradient. It utilises [RMSprop](https://machinelearningmastery.com/gradient-descent-with-rmsprop-from-scratch) strategy, which smoothens the gradient descend step by recent gradients. If there has been recently big changes in some parameter, the future change is dampened and vice versa. The RMSprop momentum is updated by exponential average on each step.

```
--- Train ---

nn = Initialise neural network parameters with chosen weight init heuristics
gradExpAvg = neural network parameters with zeroes as values

miniBatchIndex = 0
for i in 1 -> numPasses:
    numEpochs = epochs[i]
    learningRate = learningRates[i]
    Repeat numEpochs times:                                                      // O(E)
        // Pick mini batch
        for j 1 -> miniBatchSize:                                                //   O(B)
            batch[j] = trainingExamples[miniBatchIndex % len(trainingExamples)]  //     O(1)
            miniBatchIndex = miniBatchIndex + 1

        gradAvg = neural network parameters with zeroes as values                //   O(n)
        for each example in batch:                                               //   O(B)
            grad = Execute BackPropagation with example as an input              //     O(n)
            gradAvg = Add(gradAvg, grad)                                         //     O(n)
        gradAvg = Divide(gradAvg, miniBatchSize)                                 //   O(n)
                
        // Update the exponential average (RMSprop)
        gradSq = Square(grad)                                                    //   O(n)
        gradSq = Mul( gradSq, 1 - rmsPropMomentum )                              //   O(n)
        gradExpAvg = Mul( gradExpAvg, rmsPropMomentum )                          //   O(n)
        gradExpAvg = Add( gradExpAvg, gradSq )                                   //   O(n)
        
        // Calculate smoothed gradient (RMSprop)
        gradExpAvg0 = Add(gradExpAvg, epsilon)                                   //   O(n)
        gradExpAvg0 = SquareRoot(gradExpAvg0)                                    //   O(n)
        gradAvg = Divide( gradAvg, gradExpAvg0 )                                 //   O(n)
        gradAvg.multiply( gradAvg, -1.0 * learningRate );                        //   O(n)
        
        // Step training network by the gradient
        nn = Add( nn, gradAvg )                                                  //   O(n)
}
```

The algorithm stores intermediate values into constant number of neural network parameter structures with same size as the input network, thus space complexity is same as the neural network space complexity, `O(n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N))`.

The algorithm does training iterations for `E` epochs that is total number of epoch in all passes. For each epoch, it constructs the mini batch of size `B`, and for each example in the mini batch it runs the BackPropagation algorithm. Then it does few calculation operations of time complexity `O(n)`

The time complexity for the whole training is then:
`O(E * B * n) = O(E * B * (n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N))`

### Image processing

Before running the training, the app does some processing for the image. The processing algorithm finds a bounding box for the image, translates it to the center and upscales it to fit the image bounds. Upscaling uses bilinear filtering.

```
--- ProcessImage ---
Input: image
Output: processedImage

for each pixel in the image:
    if pixel above threshold:
        minX = min(x, minX)
        minY = min(y, minY)
        maxX = min(x, minX)
        maxX = min(y, miny)
       
width = maxX - minX + 1
height = mixY - minY + 1
xDelta = minX - (imageWidth - width) / 2;
yDelta = maxX - (imageHeight - height) / 2;

xscale = imageWidth / bounds.width;
yscale = imageHeight / bounds.height;
scale = min(xscale, yscale);

for each pixel:
    newX = x + translateX;
    newY = y + translateY;
    if newX < 0 || newX >= width || newY < 0 || newY >= height:
        translatedPixels(x, y) = 0;
    else:
        translatedPixels(x, y) = pixels(newX, newY);

for each pixel:
    sourceX = (x - imageWidth / 2) / scale + imageWidth / 2;
    sourceY = (y - imageHeight / 2) / scale + imageHeight / 2;

    sourceXInt = floor(sourceX);
    sourceYInt = floor(sourceY);

    double p00 = translatedPixels(sourceXInt, sourceYInt);
    double p01 = translatedPixels(sourceXInt, sourceYInt + 1);
    double p10 = translatedPixels(sourceXInt + 1, sourceYInt);
    double p11 = translatedPixels(sourceXInt + 1, sourceYInt + 1);

    xt = sourceX - sourceXInt;
    yt = sourceY - sourceYInt;

    p0 = p00 * (1 - xt) + p10 * xt;
    p1 = p01 * (1 - xt) + p11 * xt;
    p = p0 * (1 - yt) + p1 * yt;

    processedImage(x, y) = p;

```

The algorithm stores pixels to intermediate picture `translatedPixels`, so space complexity is `O(imageWidth * imageHeight)`. The algorithm could be modified to work without the translated intermediate picture relatively easy, so space complexity could be optimized to `O(1)` too.

The algorithm iterates image pixels couple of times, so time complexity is also `O(imageWidth * imageHeight)`


## Issues and possible improvements

Some known issues:
 - Training accuracy is quite sensitive to initial weights, resulting in big variance of network accuracy between weight initializations.
 - I managed to get best results with uniform weight initialization, even though literature suggests that He formula should give the best results. I don't know why that happens.

Some examples of features that could have been implemented:
 - Softmax activation with cross-entropy loss function.
   - Many sources say that for classification network cross-entropy loss function results into faster and more accurate training. Current implementation uses mean squared error as loss function.
 - Batch normalization
   - Batch normalization is said to make the network more stable and less sensitive to weight initialisation radomness, especially on deep networks.
 - Threading
   - An easy performance win for training time would've been to parallelize the training code. For instance calculating gradients would be easy to put in their own threads for each training example.
 - Deskewing
   - Many MNIST classifier implementations have gotten good results by deskewing the input set images. It would've been relative simple to implement.



