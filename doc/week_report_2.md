# Week Report 2

## Week Summary

Total hours worked: 9,5 hours

### Current status

I managed to implement backpropagation algorithm, that works at least in very simple use cases. The implementation wasn't very complicated, but most of the work time went into studying and understanding the mathematical background of the backpropagation. Understanding the mathematical background was pretty much essential for debugging problems with the implementation - and learning about it felt rewarding.

I also had time to play around with different learning strategies and hyper parameters when writing test for trainEpoch method. Sometimes it wasn't very obvious if problems with training the network was due to malfunctioning algorithm, or simply a matter of network converging into a suboptimal local minimum. However it was fun to play around with and I felt I gained understanding on how basic neural networks and training works.

The app doesn't have any kind of UI yet and it's not runnable at all. So far all the routines have been developed and tested with unit tests.

Measuring code coverage and generating javadocs has been also implemented. The documents are generated with GitHub actions on every push, and repository front page has links to the latest reports.

### Next steps

It seems like the backpropagation is working as intended, so I'm planning to move on to use MNIST data for tweaking proper training strategy. For this I think it's time to start developing basic UI, so running and tweaking the training algorithm becomes practical.

Current implementation may also be too inefficient for more complex networks, so optimizing the data structures, using efficient linear algebra routines, etc., may be necessary.

### Problems and questions

I'm having issues configuring Java Checkstyle. I've tried with few XML conf for Checkstyle from internet and they keep giving "Unable to create Root Module" errors.

Not sure how to test {@code toString()} methods. Is it even necessary? They hurt the code coverage metric a bit.

## Week notes

### Wed 27.7.2022 - 0,5 hours
 - Starting to draft the most central data structure, FeedForwardNeuralNetwork
 - Wrote first test for the FeedForwardNeuralNetwork for feedforward algorithm.

### Thu 28.7.2022 - 4 hours
 - Continuing to implement FeedForwardNeuralNetwork
 - Experimenting manually with different activation functions and biases, trying to tweak manually network giving correct result for XOR operation.
   - Learning how activation functions, weights and biases change the output of the network.
 - Add test code coverage badge and report download link to README.md
 - Studying about backpropagation
 - Starting to draft backpropagation algorithm.

### Fri 29.7.2022 - 5 hours
 - Studying more about details of backpropagation.
 - First implementation of backpropagation done.
   - The implementation is not very efficient, but the focus at this point is in clarity, and to "just to make it work." Plan is to optimize later with real-world use cases.
 - Generate javadocs and upload it with GitHub actions
 - Write javadocs for public interface.

### Sat 30.7.2022 - 0,5 hours
 - Wrapping up week's work, writing week report, doing final code tweaks.
