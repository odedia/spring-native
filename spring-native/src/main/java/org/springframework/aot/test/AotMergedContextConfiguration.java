/*
 * Copyright 2019-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.aot.test;

import java.util.Collections;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;
import org.springframework.test.context.MergedContextConfiguration;

/**
 * {@link MergedContextConfiguration} implementation based on an AOT-generated
 * {@link ApplicationContextInitializer} that is used to load an AOT-generated
 * {@link org.springframework.context.ApplicationContext ApplicationContext}.
 *
 * <p>The {@link #getParent() parent} may optionally be set as well.
 *
 * <p>An {@code ApplicationContext} should not be loaded using the metadata in
 * this {@code AotMergedContextConfiguration}. Rather the metadata from the
 * {@linkplain #getOriginal() original} {@code MergedContextConfiguration} should
 * be used.
 *
 * @author Sam Brannen
 */
class AotMergedContextConfiguration extends MergedContextConfiguration {

	private static final long serialVersionUID = 1963364911008547843L;

	private final Class<? extends ApplicationContextInitializer<?>> contextInitializerClass;

	private final MergedContextConfiguration original;


	AotMergedContextConfiguration(Class<?> testClass,
			Class<? extends ApplicationContextInitializer<?>> contextInitializerClass,
			MergedContextConfiguration original, AotCacheAwareContextLoaderDelegate cacheAwareContextLoaderDelegate,
			@Nullable MergedContextConfiguration parent) {

		super(testClass, null, null, Collections.singleton(contextInitializerClass), null, null,
			cacheAwareContextLoaderDelegate, parent);
		this.contextInitializerClass = contextInitializerClass;
		this.original = original;
	}

	/**
	 * Get the original {@link MergedContextConfiguration} that this
	 * {@code AotMergedContextConfiguration} was created for.
	 */
	MergedContextConfiguration getOriginal() {
		return this.original;
	}

	@Override
	public boolean equals(@Nullable Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || other.getClass() != getClass()) {
			return false;
		}

		AotMergedContextConfiguration that = (AotMergedContextConfiguration) other;
		if (!this.contextInitializerClass.equals(that.contextInitializerClass)) {
			return false;
		}

		if (getParent() == null) {
			if (that.getParent() != null) {
				return false;
			}
		}
		else if (!getParent().equals(that.getParent())) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = this.contextInitializerClass.hashCode();
		result = 31 * result + (getParent() != null ? getParent().hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
				.append("testClass", getTestClass().getName())
				.append("contextInitializerClass", this.contextInitializerClass.getName())
				.append("original", this.original)
				.append("parent", getParent())
				.toString();
	}

}
