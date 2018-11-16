import numpy as np
from keras.preprocessing.image import ImageDataGenerator
from keras.models import Sequential
from keras.layers import Dropout, Flatten, Dense
from keras import applications
import keras
import json
from keras.callbacks import History
from keras import backend as K

# dimensions of our images.
img_width, img_height = 224, 224
nb_train_samples = 2000
nb_validation_samples = 400
epochs = 50
batch_size = 16


def save_bottleneck_features(dirs, saved):
    datagen = ImageDataGenerator(rescale=1. / 255)

    # build the VGG16 network
    model = applications.VGG16(include_top=False, weights='imagenet')

    generator = datagen.flow_from_directory(
        dirs[0],
        target_size=(img_width, img_height),
        batch_size=batch_size,
        class_mode=None,
        shuffle=False)
    bottleneck_features_train = model.predict_generator(
         generator, nb_train_samples // batch_size) #//batch_size
    
    np.save(saved[0],
            bottleneck_features_train)

    generator = datagen.flow_from_directory(
        dirs[1],
        target_size=(img_width, img_height),
        batch_size=batch_size,
        class_mode=None,
        shuffle=False)
    bottleneck_features_validation = model.predict_generator(
        generator, nb_validation_samples // batch_size) #
    np.save(saved[1],
            bottleneck_features_validation)


def train_top_model(saved, weight, files):
    train_data = np.load(saved[0] + '.npy')
    train_labels = np.array(
        [0] * (nb_train_samples // 2) + [1] * (nb_train_samples // 2))  

    validation_data = np.load(saved[1] + '.npy')
    validation_labels = np.array(
        [0] * (nb_validation_samples // 2) + [1] * (nb_validation_samples // 2))

    early_stopping  = keras.callbacks.EarlyStopping(monitor='val_loss',
                                                    min_delta=0,
                                                    patience=4,
                                                    verbose=1, mode='auto')

    hist = History()

    model = Sequential()
    model.add(Flatten(input_shape=train_data.shape[1:]))
    model.add(Dense(4096, activation='softmax'))
    model.add(Dense(4096, activation='relu'))
    model.add(Dense(1, activation='sigmoid'))
    #model.add(Dense(256, activation='relu'))
    #model.add(Dropout(0.5))
    #model.add(Dense(1, activation='sigmoid'))

    model.compile(optimizer='rmsprop',
                  loss='binary_crossentropy', metrics=['accuracy'])

    model.fit(train_data, train_labels,
              epochs=epochs,
              batch_size=batch_size,
              validation_data=(validation_data, validation_labels),
              callbacks=[early_stopping, hist])
    model.save_weights(weight)
    with open(files, "w") as f:
        f.write(json.dumps(hist.history))
        f.write("\nEpochs: " + str(len(hist.history["val_acc"])))
    print(hist.history)


#save_bottleneck_features()
#train_top_model()

paths = [("trainEc", "validationEc"), ("trainHi", "validationHi"),("trainPs", "validationPs"),("trainRw", "validationRw"),("trainSh", "validationSh")]
bottlenecks = [("bottleneck_Ec", "validation_Ec"), ("bottleneck_Hi", "validation_Hi"),("bottleneck_Ps", "validation_Ps"),("bottleneck_Rw", "validation_Rw"),("bottleneck_Sh", "validation_Sh")]
weights = ["fc_Ec.h5", "fc_Hi.h5", "fc_Ps.h5", "fc_Rw.h5", "fc_Sh.h5"]
txtfile = ["Ec.txt", "Hi.txt","Ps.txt","Rw.txt","Sh.txt"]

for i in range(len(paths)):
    print("here")
    save_bottleneck_features(paths[i], bottlenecks[i])
    print("done saving now to train")
    train_top_model(bottlenecks[i], weights[i], txtfile[i])
    K.clear_session()

print("Done training")    
