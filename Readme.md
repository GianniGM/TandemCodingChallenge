# Tandem Coding challenge

## MVVM

Using jetpack means embracing the new google way of making Android apps, which means using Model View ViewModel.
* Model: the Repository and everything needed to populate the repository
* View: Our View is represented by the Activity and all the Jetpack compose UI logic
* ViewModel: in this case, it is just a glue code between the Activity and the Model

## Used Libraries

Here is a list of libraries I have used to implement the app

### (Why???) Compose

I like challenges, a coding challenge is also a way to use new technologies that I don't use in my daily life.
Since using Jetpack was nice to have in this, I have decided to use Compose.
I see a lot of potential in implementing declarative UI

### Flow

The reactive pattern is the best way for implementing an app, either using Rx Java or Flow. Since using jetpack was nice to have
I have chosen to use Flow instead of RxJava. I'm more fluent with RxJava but I find flow more harmonised with MVVM and Compose.

### Room

From the coding challenge, it was requested to store the likes of the user.
The better way of doing it is using a database via Room.
This also brings a better experience for the user, they can save their likes but also they can check the users' list when offline.

### Paging

Paging is the library if the need is to scroll between pages and provide a feeling of infinite scrolling to the user.
Paging is perfect when it is needed to make API calls to request the following request meanwhile the user is scrolling through items in a list.


## Why no Unit tests?

I love unit tests, but in this case, there is nothing to do,
since I'm using paging everything is done by it.
The part that I consider significant to test was the merging between the DB list and the list of users
received from the API, to update the "like" field.

## Challenges

### Using paging

It's been a while since I don't use paging, I see since the last time I have used it, lots of things have changed.
I've made a mistake during the development that caught me a bit unprepared. Since I wasn't very experienced with Paging, I thought was a good idea to add paging only after implementing the API calls and the DB.
So I have created a state like this:

```kotlin
sealed interface ViewModelState {

    object Loading: ViewModelState

    data class ShowErrorMessage(val errorText: String? = null) : ViewModelState

    data class ShowUserList(val userList: PagingData<TandemUser>) : ViewModelState

    object Empty: ViewModelState
}
```
Realising only afterwards, that paging already provides `LoadResult` that makes everything for me.

### Using Compose

It took a bit more time than usual to implement the UI since I am still in the XML mindset.
Though, it was fun. I have found complex to keep the code clean, and I am thinking that composing the UI code in a good way needs to follow
some architectural pattern. I have tried to do my best to make the compose part cleaner.

## What's missing if we want to push on PROD?

Here is a list of missing things that might be needed to be added if we want to push this app into production, to provide a better code scalability

### DI

The more classes we add the more it is important to use Dependency Injection, there are mainly two DI processors here that we can use:
* Android hilt (Jetpack)
* Dagger 2
  Setting up the DI processor takes always a bit, in this case, I have chosen to not use it, we can inject already the ViewModel using the factory and for this use, the case is not needed to set up an entire DI processor with @ActivityScope, @ViewScope and @ApplicationScope

### Dark Theme

Dark theme is the new trend and would be nice to have the Dark Side of the app

### DB versioning

Sometimes DBs like to change, and just adding a new field without putting its versioning will make the app crash,
important to handle versioning on the app DB.

### CI

Continuous integration is very important for code checking, maintenance and building.
It helps to keep the main or developed branch healthy whenever new code is merged or pushed from feature branches

### Instrumentation tests

Unfortunately, I have a very poor experience with UI tests, I'm not used to writing them in my daily life, usually I use appium for them or just record the instrumentation test using android studio.

### Feedbacks

I find that sending a 404 Error when reaching the end of pagination, is not really a good idea.
I'd preferred having an api with the following link
```
https://tandem2019.web.app/api/{page_number}/community.json
```
and adding a field in the API like this

```
  {
    "response" : []
    "errorCode": null,
    "type": "error"
    "limit_reached": true
  }
```
In this case I can check from paging if the limit is reached instead of having a 404.
I think is better to send a 404 for real backend issues, for example, if an endpoint is not working properly.
From the app side I can handle the exception and showing the data coming from the backend
