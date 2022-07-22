# Project definition

My study programme is "Tietojenkäsittelytieteen kandiohjelma".

Project will be written in Java. I'm also capable to peer review projects written in C++, C# and Python. I'll use English as documentation language.

## Project goal and motivation

Aim of the project is to recognize hand-written numbers. For this problem, I'm planning to implement basic feedforward neural network data structure. For learning the input set I'm planning to implement backpropagation algorithm.

While artificial neural network may be a bit overkill solution for the problem, I chose to implement it due to personal interest on the topic. Modern AIs and what they are capable of fascinates me and many of them utilizes some sort of artificial neural network. I thought this would be a great opportunity to gain some basic insight on  some principles of this machine learning method.

## Time and space complexity

Space complexity for algorithms depends on neural network architecture and learning parameters. 

Neural network data structure will have following parameteres:
- Neural network layers, N
- Number of nodes at layer i, n_i

Since each node at layer x will have n_(x+1) outgoing edges, Space complexity for storing the neural network would be: `O( n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N )`

Backpropagation learning algorithm will have following parameters affecting time complexity:
 - Training iterations, "epochs", E
 - Number of training examples, T
 
Looking at example implementation at ai stack exchange, I find reasonable target for time complexity for backpropagation to be: `O( E * T * ( n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N  ) )`

Space complexity will be same as the neural network data structure.

For classifying number on trained neural network, target time and space complexity will be `O( n_1 * n_2 + n_2 * n_3 + ... + n_(N-1) * n_N )`.

For this particular problem, the input layer will have P neurons, where P is number of pixels in the input image, i.e. n_1 = P, and final layer will have number of possible outputs, i.e. 10 numbers, so n_N = 10.

## Program inputs

For learning the numbers, program will receive a set of pictures paired with the number as an input, for which output will be neural network parameters that can be used to classify new pictures to numbers they represent. Program will take also various hyperparameters, such as number of layers, number of neurons on each layer and training iterations.

For classification, program will receive a picture and neural network parameters generated with learning algorithm. The program will feed the picture to a neural network defined by the input parameters, and output the classification that the neural network produces. 

## References
 - https://en.wikipedia.org/wiki/Artificial_neural_network
 - https://en.wikipedia.org/wiki/Feedforward_neural_network
 - https://en.wikipedia.org/wiki/Backpropagation
 - https://ai.stackexchange.com/questions/5728/what-is-the-time-complexity-for-training-a-neural-network-using-back-propagation