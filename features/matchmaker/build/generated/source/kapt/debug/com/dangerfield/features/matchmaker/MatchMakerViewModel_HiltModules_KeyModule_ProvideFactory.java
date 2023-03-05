// Generated by Dagger (https://dagger.dev).
package com.dangerfield.features.matchmaker;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.internal.lifecycle.HiltViewModelMap.KeySet")
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class MatchMakerViewModel_HiltModules_KeyModule_ProvideFactory implements Factory<String> {
  @Override
  public String get() {
    return provide();
  }

  public static MatchMakerViewModel_HiltModules_KeyModule_ProvideFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static String provide() {
    return Preconditions.checkNotNullFromProvides(MatchMakerViewModel_HiltModules.KeyModule.provide());
  }

  private static final class InstanceHolder {
    private static final MatchMakerViewModel_HiltModules_KeyModule_ProvideFactory INSTANCE = new MatchMakerViewModel_HiltModules_KeyModule_ProvideFactory();
  }
}