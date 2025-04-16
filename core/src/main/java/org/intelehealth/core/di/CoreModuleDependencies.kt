package org.intelehealth.core.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Vaghela Mithun R. on 18-12-2024 - 18:30.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
@EntryPoint
@InstallIn(SingletonComponent::class)
interface CoreModuleDependencies {
    // fun sampleRepository(): SampleRepository
}