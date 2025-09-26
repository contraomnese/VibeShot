## About

A multi-module Android application for creating photo tasks and publishing photos to the Flickr service.
The application requires the Flickr API key and secret to work.
This product uses the Flickr API, but is not endorsed or certified by SmugMug, Inc.

## Stack

* MVI (feature tasks full implements this template)
* Koin
* Jetpack Compose
* Jetpack Navigation
* Okhttp 3
* Retrofit 2
* Room
* SQL
* Pagging 3
* Datastore
* SplashScreen
* Coil
* ChromeTabs

## Authentication

The application implements OAuth Core 1.0 Revision A authentication with signature generation, storing authenticated user data (access token and secret) in the application datastore.

https://github.com/user-attachments/assets/17ed00f6-5b13-4ba7-a646-adbaba49017c

## Interests feature

When switching to the *Interesting* tab, the user receives a list (*500 photos*) of popular photos for the current day, using the Flickr API.
Photos are saved in the storage. The storage is updated every 24 hours. With the help of **Pagging 3**, data is paired, photos are loaded 25 at a time - previously loaded photos are taken from the database, and new photos from the Internet. When clicking on a photo, a window with detailed information about it opens. In the "Detailed" window, it is also possible to scroll through photos and load them. Both windows - *Interesting* and *Detailed* are **synchronized by one stream of photos**.

https://github.com/user-attachments/assets/b82746dd-4ab6-4cee-9a35-d77503e3b8f4

## Tab to Flickr

Flickr members deserve credit for the content they create, including the useful information around their photo (such as licenses, tags, and geo location).
All of this data is maintained on the photo page on Flickr, so the app has added a link to the photo's copyright page.

https://github.com/user-attachments/assets/6013bc10-85fe-4bf9-a4f6-7604036980b7

## Tasks feature

The main idea of the application is to generate tasks for photo enthusiasts so that they have more ideas for photos.
After receiving a task, the user can either take a photo immediately or upload it from the gallery.
If necessary, the user can add a title and description for the photo. By default, the received task is added to the description.
This function is available only for users authorized in the Flickr service.

https://github.com/user-attachments/assets/3cdd8867-6ae9-400b-adb1-2d517e87d925

## Search feature

Published photos, as well as any other photos, are easy to find using search.
Also, in the "Details" window, you can click on the tag of the photo and you will go to the search screen for this tag.

https://github.com/user-attachments/assets/c8e40b79-1260-4387-8a1f-c7029eace070

https://github.com/user-attachments/assets/d1272c80-d63f-411d-9b54-10b594810d75

## Log out and clear user data

The user data, in our case the theme style and authorization tokens, are saved in the application.
Clicking on Logout takes the user back to the Authorization screen. The database with uploaded photos is also cleared.

https://github.com/user-attachments/assets/93f27aa7-df7c-4501-a924-864f3a3f50d2
