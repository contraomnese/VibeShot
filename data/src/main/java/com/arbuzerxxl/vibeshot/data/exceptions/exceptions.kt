package com.arbuzerxxl.vibeshot.data.exceptions

class RequestTokenFetchException(cause: Throwable): Throwable(cause = cause)
class RequestTokenInvalidResponseException(cause: Throwable): Throwable(cause = cause)
class RequestTokenInitializeException(cause: Throwable): Throwable(cause = cause)
class AccessTokenFetchException(cause: Throwable): Throwable(cause = cause)
class AccessTokenInvalidResponseException(cause: Throwable): Throwable(cause = cause)

class RequestInterestsPhotosFetchException(cause: Throwable): Throwable(cause = cause)

class RequestPhotosFetchException(cause: Throwable): Throwable(cause = cause)
class RequestPhotoSizesFetchException(cause: Throwable): Throwable(cause = cause)
class RequestPhotoInfoFetchException(cause: Throwable): Throwable(cause = cause)
class RequestPhotoExifFetchException(cause: Throwable): Throwable(cause = cause)