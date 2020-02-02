package com.lgorev.ksuonlineeducation.security

const val TOKEN_HEADER = "Authorization"
const val TOKEN_PREFIX = "Bearer "
const val TOKEN_TYPE_HEADER = "typ"
const val TOKEN_TYPE = "JWT"
const val TOKEN_ISSUER = "secure-api"
const val TOKEN_AUDIENCE = "secure-app"
const val ACCESS_TOKEN_LIFETIME_SECONDS = 10 *  60 * 60L
const val JWT_SECRET = "G+KbPeShVmYq3s6v9ybb&E)H@McQfTjWnZr4u7w!z%C*F-JaNdRgUkXp2s5v8y/A"
const val ROLES_CLAIM = "roles"
const val USER_ID_CLAIM = "user_id"