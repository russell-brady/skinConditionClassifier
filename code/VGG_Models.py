from keras import applications
from keras.preprocessing.image import ImageDataGenerator, load_img, img_to_array
from keras import optimizers
import numpy as np
from keras.models import Sequential
from keras.layers import Dropout, Flatten, Dense
from keras import backend as K
import os
import os.path as path
import tensorflow as tf
from tensorflow.python.tools import freeze_graph
from tensorflow.python.tools import optimize_for_inference_lib


# path to the model weights files.
weights_path = '../keras/examples/vgg16_weights.h5'
top_model_weights_path = 'fc_Rw.h5'
# dimensions of our images.
img_width, img_height = 224, 224

#MODEL_NAME = "vgg"

# train_data_dir = 'train'
# validation_data_dir = 'validation'
# nb_train_samples = 2000
# nb_validation_samples = 400
# epochs = 1
# batch_size = 16

# build the VGG16 network
model = applications.VGG16(weights='imagenet', include_top=False, input_shape = (224,224,3))
last = model.output
print('Model loaded.')

# build a classifier model to put on top of the convolutional model
top_model = Sequential()
top_model.add(Flatten(input_shape=model.output_shape[1:]))
top_model.add(Dense(4096, activation='softmax'))
top_model.add(Dense(4096, activation='relu'))
top_model.add(Dense(1, activation='sigmoid'))


top_model.load_weights(top_model_weights_path)

new_model = Sequential()
for l in model.layers:
    new_model.add(l)


for layer in top_model.layers:
    new_model.add(layer)

#print(new_model.summary())

for layer in new_model.layers[:15]:
    layer.trainable = False

new_model.save("VGG_Ringworm.h5")
print("model saved --------------------")   


   