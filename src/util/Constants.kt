package com.mrwhoknows.util

object Constants {

    const val DATABASE_URI = "jdbc:postgresql://localhost:5432/postgres"
    const val DATABASE_DRIVER = "org.postgresql.Driver"
    //    TODO GET it from env vars
    const val DATABASE_USER = "postgres"
    const val DATABASE_PASSWORD = "password"

    // Dummy data
    const val DUMMY_CONTENT =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut" +
                " labore et dolore magna aliqua. Dolor sit amet consectetur adipiscing elit." +
                " In fermentum et sollicitudin ac orci phasellus."

    const val DUMMY_THUMBNAIL = "https://images.pexels.com/photos/1714208/pexels-photo-1714208.jpeg?" +
            "cs=srgb&dl=pexels-josh-sorenson-1714208.jpg&fm=jpg"

    const val DUMMY_AUTHOR = "WhoKnows"
    const val DUMMY_TAGS = "tag1,tag2"
    const val DUMMY_TITLE = "This is a great title"
    const val DUMMY_DESCRIPTION = "This is some description"

}