/*
 *    Copyright 2012 Werner Bayer
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package at.pardus.android.webview.gm.demo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;

import java.util.EmptyStackException;
import java.util.Stack;

import at.pardus.android.webview.gm.model.ScriptId;
import at.pardus.android.webview.gm.store.ScriptStoreSQLite;
import at.pardus.android.webview.gm.store.ui.ScriptBrowser;
import at.pardus.android.webview.gm.store.ui.ScriptEditor;
import at.pardus.android.webview.gm.store.ui.ScriptList;
import at.pardus.android.webview.gm.store.ui.ScriptManagerActivity;

/**
 * Implements all parts of the WebView GM library.
 */
public class WebViewGmImpl extends ScriptManagerActivity {

	private Stack<Integer> placeHistory = new Stack<Integer>();

	private SharedPreferences preferences;

	private static final Integer LIST = 1;

	private static final Integer BROWSER = 2;

	@Override
	public void openScriptList() {
		if (scriptList == null) {
			if (scriptStore == null) {
				scriptStore = new ScriptStoreSQLite(this);
				scriptStore.open();
			}
			scriptList = new ScriptList(this, scriptStore);
		}
		setTitle(R.string.webviewgm_impl_app_name);
		setContentView(scriptList.getScriptList());
		placeHistory.push(LIST);
	}

	@Override
	public void openScriptEditor(ScriptId scriptId) {
		if (scriptEditor == null) {
			if (scriptStore == null) {
				scriptStore = new ScriptStoreSQLite(this);
				scriptStore.open();
			}
			scriptEditor = new ScriptEditor(this, scriptStore);
		}
		setContentView(scriptEditor.getEditForm(scriptId));
		placeHistory.push(null);
	}

	@Override
	public void openScriptBrowser() {
		if (scriptBrowser == null) {
			if (scriptStore == null) {
				scriptStore = new ScriptStoreSQLite(this);
				scriptStore.open();
			}
			scriptBrowser = new ScriptBrowser(this, scriptStore,
					preferences.getString("lastUrl", "https://greasyfork.org/"));
		}
		setContentView(scriptBrowser.getBrowser());
		placeHistory.push(BROWSER);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.impl_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_browse) {
			openScriptBrowser();
			return true;
		} else if (item.getItemId() == R.id.menu_list) {
			openScriptList();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_PROGRESS);
		preferences = getSharedPreferences("P", Context.MODE_PRIVATE);
		openScriptBrowser();
	}

	@Override
	protected void onResume() {
		if (scriptStore != null) {
			scriptStore.open();
		}
		if (scriptBrowser != null) {
			scriptBrowser.resume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		if (scriptBrowser != null) {
			SharedPreferences.Editor editor = preferences.edit();
			editor.putString("lastUrl", scriptBrowser.getUrl());
			editor.commit();
			scriptBrowser.pause();
		}
		if (scriptStore != null) {
			scriptStore.close();
		}
		super.onPause();
	}

	@Override
	public void onBackPressed() {
		try {
			Integer thisPlace = placeHistory.pop();
			if (BROWSER.equals(thisPlace) && scriptBrowser.back()) {
				placeHistory.push(thisPlace);
			} else {
				while (true) {
					Integer prevPlace = placeHistory.pop();
					if (prevPlace == null || prevPlace.equals(thisPlace)) {
						continue;
					}
					if (LIST.equals(prevPlace)) {
						openScriptList();
						return;
					}
					if (BROWSER.equals(prevPlace)) {
						openScriptBrowser();
						return;
					}
				}
			}
		} catch (EmptyStackException e) {
			super.onBackPressed();
		}
	}

}
