# Split: *The new way to split the bill*

## Inspiration


## What it does
Split allows a group to divide up the costs of a bill with ease. By using an Optical Character Recognition and the simple  camera of your phone, Split can recognize its contents and make sense of the context of the transactions. The user can simply add the group of interest to the transactions and be on the way to splitting the bill fairly on an item-by-item basis.

Split also has a semi-Venmo (Nessie) integration that allows for money requests to be automatically made based on the split of the bill. All of the tediousness of current-day bill splitting is alleviated with this smart implementation of a century-long problem.

## How we built it
We built the app using Android Studio and Google's Cloud Vision API. The main language used was Java along with architecturally integrated Kotlin and C++. Google Firebase was also used to make the OCR functional and work in real time along with the many other aspects of this application.

## Challenges we ran into
There were a plethora of concerns and conflicts we ran into throughout the development process. The biggest challenge was making everything work in real-time and allowing the machine learning model to pass values of real world receipts to the data pipeline of the application.

Furthermore, we had a tough time making the bill splitting process dynamic and update throughout the process of the various user interactions.

## Accomplishments that we're proud of
When we started building this application, we were proud of the idea, but we were not readily sure of our abilities to carry out the high promise. As we solved the many merge conflicts and silly bugs, we became more and more invested in the idea. The final testing of the product was a moment of eureka, when all our hard labor came together to demo our initial vision. The real-world application aspect of Split surpassed our expectations and we truly felt as though this was an app we would have on the front page of our smartphones.

## What we learned
We refined our beginner level skill of mobile app development, specifically with Android Studio. We also learned how to integrate the camera application with mobile apps and use the cached data to build a model. Google Vision and the Nessie API were also something we had to build our knowledge base up around, ultimately getting comfortable enough to relay our newfound skills to one another.

## What's next for Split
We really wanted to integrate Venmo into our application, but sadly the way their API is set up does not allow easy integrations for mobile apps, let alone being friendly for a hackathon development timeline. We also want to refine our model of receipt recognition, ideally create our own OCR specialized for our use case.
