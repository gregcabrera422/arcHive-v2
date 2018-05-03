package com.untitledhorton.archive.Utility;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Greg on 03/05/2018.
 */

public interface FirebaseCommand {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    DatabaseReference NOTES_TABLE = FirebaseDatabase.getInstance().getReference().child("Notes").child(user.getUid());
}
