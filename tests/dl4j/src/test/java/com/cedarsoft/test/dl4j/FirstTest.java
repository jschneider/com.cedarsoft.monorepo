package com.cedarsoft.test.dl4j;

import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class FirstTest {
  public void testIt() {
    MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                                     //.iterations(1)
                                     .weightInit(WeightInit.XAVIER)
                                     .activation(Activation.RELU)
                                     .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                                     //.learningRate(0.05)
                                     // ... other hyperparameters
                                     .list()
                                     .backprop(true)
                                     .build();


  }

}
