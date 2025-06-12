# VibeShotApp

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

![](https://i.imgur.com/fYZWsKd.gif)]

## Interests feature

When switching to the *Interesting* tab, the user receives a list (*500 photos*) of popular photos for the current day, using the Flickr API.
Photos are saved in the storage. The storage is updated every 24 hours. With the help of **Pagging 3**, data is paired, photos are loaded 25 at a time - previously loaded photos are taken from the database, and new photos from the Internet. When clicking on a photo, a window with detailed information about it opens. In the "Detailed" window, it is also possible to scroll through photos and load them. Both windows - *Interesting* and *Detailed* are **synchronized by one stream of photos**.

![](https://i.imgur.com/mELbXY8.gif)]

## Tab to Flickr

Flickr members deserve credit for the content they create, including the useful information around their photo (such as licenses, tags, and geo location).
All of this data is maintained on the photo page on Flickr, so the app has added a link to the photo's copyright page.

![](https://i.imgur.com/u8rjV06.gif)]

## Tasks feature

The main idea of the application is to generate tasks for photo enthusiasts so that they have more ideas for photos.
After receiving a task, the user can either take a photo immediately or upload it from the gallery.
If necessary, the user can add a title and description for the photo. By default, the received task is added to the description.
This function is available only for users authorized in the Flickr service.

![](https://i.imgur.com/CM82tTv.gif)]

## Search feature

Published photos, as well as any other photos, are easy to find using search.
Also, in the "Details" window, you can click on the tag of the photo and you will go to the search screen for this tag.

![](https://i.imgur.com/8JG9pHQ.gif)]

![](https://i.imgur.com/q3hITzA.gif)]

## Log out and clear user data

The user data, in our case the theme style and authorization tokens, are saved in the application.
Clicking on Logout takes the user back to the Authorization screen. The database with uploaded photos is also cleared.

![](https://i.imgur.com/yLAwXqo.gif)]