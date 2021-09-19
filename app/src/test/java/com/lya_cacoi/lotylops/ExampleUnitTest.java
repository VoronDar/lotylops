package com.lya_cacoi.lotylops;

import android.provider.Telephony;

import com.lya_cacoi.lotylops.SenteceCheck.SentenceChecker;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.FileReader;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {

    @Test()
    public void checkSentenceDecoder() {


        assertThat(getDecoded("[s:she]!pv:wants=0]av:live*to=1]"), is("she wants to live"));
        assertThat(getDecoded("[s:she]pv:fly-must not=0]"), is("she must not fly"));
        assertThat(getDecoded("[s:i]pv:did=0]an:movement*this=1]ad:hard*really=2]sv:prove*to/that=1]s:i=4]pv:deal-can=5]sn:difficulties*with=6]ad:any=7]"), is("i did this really hard movement to prove that i can deal with any difficulties"));

        assertThat(getDecoded(""), is(""));
        assertThat(getDecoded(""), is(""));
        assertThat(getDecoded(""), is(""));
        assertThat(getDecoded(""), is(""));
        assertThat(getDecoded(""), is(""));
    }

    private String getDecoded(String decoding) {
        return SentenceChecker.generateRightStringFromLib(decoding).substring(1);
    }
}