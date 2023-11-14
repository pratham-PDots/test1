import React, { useState, useEffect } from 'react';
import { NativeEventEmitter, SafeAreaView, StyleSheet, Text, TouchableOpacity, View, FlatList, Image } from 'react-native';
import { Colors } from 'react-native/Libraries/NewAppScreen';

import { NativeModules } from 'react-native';
const { CameraModule } = NativeModules;

interface DataType {
  images: { uri: string }[];
  session_id: string;
}

function App(): JSX.Element {
  const [events, setEvents] = useState<DataType[]>([]);

  const handleOpenCam = () => {
    CameraModule.openCam();
  };

  useEffect(() => {
    // Initialize the SDK when the component mounts
    CameraModule.initSDK();

    // Set up the event listener
    const eventEmitter = new NativeEventEmitter(NativeModules.CameraModule);
    let eventListener = eventEmitter.addListener('did-receive-queue-data', (eventsData: DataType[]) => {
      setEvents(eventsData);
    });

    // Removes the listener once unmounted
    return () => {
      eventListener.remove();
    };
  }, []); // The empty dependency array ensures that this effect runs only once on mount

  const renderHeader = ({ item }: { item: DataType }) => (
    <View style={styles.headerContainer}>
      <Text style={styles.sessionId}>Session ID: {item.session_id}</Text>
    </View>
  );

  const renderImage = ({ item }: { item: DataType }) => (
    <FlatList
      data={item.images}
      keyExtractor={(imageItem) => imageItem.uri}
      renderItem={({ item: image }) => (
        <Image source={{ uri: `file://${image.uri}` }} style={styles.image} />
      )}
      horizontal
      showsHorizontalScrollIndicator={false}
    />
  );

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.buttonContainer}>
        <TouchableOpacity onPress={handleOpenCam} style={styles.button}>
          <Text style={styles.buttonText}>Open Camera</Text>
        </TouchableOpacity>
      </View>

      <FlatList
        data={events}
        keyExtractor={(item) => item.session_id}
        renderItem={({ item }) => (
          <View>
            {renderHeader({ item })}
            {renderImage({ item })}
          </View>
        )}
      />
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  buttonContainer: {
    marginTop: 20,
  },
  button: {
    backgroundColor: Colors.primary,
    padding: 20,
    borderRadius: 10,
  },
  buttonText: {
    color: Colors.white,
    fontSize: 18,
    fontWeight: 'bold',
    textAlign: 'center',
  },
  headerContainer: {
    backgroundColor: Colors.lighter,
    padding: 10,
  },
  sessionId: {
    fontSize: 20,
    fontWeight: 'bold',
    marginTop: 10,
  },
  image: {
    width: 100,
    height: 100,
    margin: 5,
  },
});

export default App;
