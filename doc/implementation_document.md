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

The program implements loading an image dataset (MNIST file format is supported), running a neural network training algorithm, and classifying a user drawn image with the trained neural network.

### Training/Test image processing

Images are centered and scaled to fit the image bounds. This reduces errors especially with user drawn images, that vary easily in size and positioning.

### Training the neural network

Training algorithm is standard stochastic gradient descent. Network gradients are calculated with backpropagation algorithm. Training rate schedule is configurable.

## Implementatino details, time and space complexities

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

TODO: Space and time complexity analysis.

## Training algorithm

TODO: Inputs, outputs, description

```
--- Train ---
Repeat E times:
    // Pick mini batch
    for j 1 -> miniBatchSize:
        batchIndices[j] = random value between 1...number of trainingExamples
        isDuplicate = false;
        for k 1 -> (j-1):
            if batchIndices[k] == batchIndices[j]:
                isDuplicate = true;
        if isDuplicate:
            repeat

    for j 1 -> miniBatchSize:
        batch[j] = trainingExamples[batchIndices[j]]

    for each example in batch:
        Execute BackPropagation with example as an input
        for l 1 -> N:
            for i 1 -> n_l:
                b_li = b_li - Db_li * learningRate / miniBatchSize
        for l 1 -> (N-1):
            for i 1 -> n_l:
                for j -> n_(l+1):
                    w_l_ij = w_l_ij - Dw_l_ij * learningRate / miniBatchSize

}
```

TODO: Space and time complexity analysis.

## Flaws and improvements

Currently the application is heavily work in progress.
