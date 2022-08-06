# Week Report 3

## Week Summary

Total hours worked: 10 hours

Project has been progressing as planned. The algorithm worked with MNIST dataset surprisingly well on the first go. The app is able to train the network and test it against the MNIST dataset, and there is a simple interactive app where user can draw their own numbers and networks tries to guess it. Even though the tested network performs relatively well against MNIST test dataset (~97% accuracy), it's struggling with some hand-written numbers. E.g., it seems to have lots of issues recognizing number 9, at least with my hand writing(, or mouse writing). There is lot that can be done for that though, the mouse painting could produce similar line than with the dataset, with some smoothing, etc. and the drawn number could be scaled to fit image bounds better.

Core components of the project has been implemented. Now it's just matter of improvement and filling in missing holes (that there are a lot). For next week i'm planning to:
 - Implement better UI. Training and number drawing should probably be in same App. Now they are split into two different apps and training is command-line only.
 - Ability to more conveniently train different networks with different parameters and compare their accuracy.
 - Improve accuracy of the interactive number guessing.
 - Improve speed of training the network.
 - Improve unit testing.

## Week notes

Tue 2.8.2022 - 1 hours
 - Implement loader for MNIST image label pairs.

Wed 3.8.2022 - 0,5 hours
 - Start drafting TestConfig, that is going to be used for defining training hyperparameters.

Thu 4.8.2022 - 3,5 hours
 - Finish implementing TestConfig
 - First actual runs with MNIST data. Playing around with hyper parameters.

Fri 5.8.2022 - 3,5 hours
 - Improving unit test coverage
 - First implementation for the interactive app

Sat 6.8.2022 - 1,5 hour
 - Wrapping up week's work
 - Writing test documentation