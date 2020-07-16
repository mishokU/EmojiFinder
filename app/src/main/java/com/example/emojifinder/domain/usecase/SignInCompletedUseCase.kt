/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.emojifinder.domain.usecase

import com.example.emojifinder.domain.UseCase
import com.example.emojifinder.data.db.remote.service.FirebaseAuthHandler
import javax.inject.Inject

/**
 * Returns whether sign in has been completed.
 */
open class SignInCompletedUseCase @Inject constructor(
    private val auth : FirebaseAuthHandler
) : UseCase<Unit, Boolean>() {
    override fun execute(parameters: Unit): Boolean = auth.isLogged()
}
