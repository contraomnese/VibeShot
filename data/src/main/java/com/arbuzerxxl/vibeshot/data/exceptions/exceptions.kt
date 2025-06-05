package com.arbuzerxxl.vibeshot.data.exceptions

class RequestTokenFetchException(cause: Throwable): Exception(cause)
class RequestTokenInvalidResponseException(cause: Throwable): Exception(cause)
class RequestTokenInitializeException(cause: Throwable): Exception(cause)
class AccessTokenFetchException(cause: Throwable): Exception(cause)
class AccessTokenInvalidResponseException(cause: Throwable): Exception(cause)

class RequestInterestsPhotosFetchException(cause: Throwable): Exception(cause)

class RequestPhotosFetchException(cause: Throwable): Exception(cause)
class RequestSearchPhotosFetchException(cause: Throwable): Exception(cause)
class RequestPhotoSizesFetchException(cause: Throwable): Exception(cause)
class RequestPhotoInfoFetchException(cause: Throwable): Exception(cause)

class RequestMoodInitializeException(cause: Throwable): Exception(cause)
class RequestSeasonInitializeException(cause: Throwable): Exception(cause)
class RequestTopicInitializeException(cause: Throwable): Exception(cause)
class RequestPhotoTaskFetchException(cause: Throwable): Exception(cause)

class RequestUploadPhotoException(cause: Throwable): Exception(cause)