/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/*******************************************************************************
 * This code has been modified
 ******************************************************************************/

package caseus.dragonboattesting.examples;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;

import com.badlogic.gdx.Gdx;

import caseus.dragonboattesting.GdxTestRunner;

@RunWith(GdxTestRunner.class)
public class AssetExistsExampleTest {
	@Test
	public void backgroundSpriteExists() {
		Assertions.assertTrue(
			Gdx.files.internal("../core/assets/background sprite.png").exists(),
			"This test will only pass when the specified file exists."
		);
	}
}
