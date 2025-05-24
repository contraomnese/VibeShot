package com.arbuzerxxl.vibeshot.data.exceptions

class RequestTokenFetchException(cause: Throwable): Throwable(cause = cause)
class RequestTokenInvalidResponseException(cause: Throwable): Throwable(cause = cause)
class RequestTokenInitializeException(cause: Throwable): Throwable(cause = cause)
class AccessTokenFetchException(cause: Throwable): Throwable(cause = cause)
class AccessTokenInvalidResponseException(cause: Throwable): Throwable(cause = cause)

class RequestInterestsPhotosFetchException(cause: Throwable): Throwable(cause = cause)

class RequestPhotosFetchException(cause: Throwable): Throwable(cause = cause)
class RequestSearchPhotosFetchException(cause: Throwable): Throwable(cause = cause)
class RequestPhotoSizesFetchException(cause: Throwable): Throwable(cause = cause)
class RequestPhotoInfoFetchException(cause: Throwable): Throwable(cause = cause)

class RequestMoodInitializeException(cause: Throwable): Throwable(cause = cause)
class RequestSeasonInitializeException(cause: Throwable): Throwable(cause = cause)
class RequestTopicInitializeException(cause: Throwable): Throwable(cause = cause)
class RequestPhotoTaskFetchException(cause: Throwable): Throwable(cause = cause)