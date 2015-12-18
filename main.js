/**
 * @method userByUsername
 *
 * Returns Parse User if there is a user with that username, else nil
 * @note Does not send an error if there is no result
 *
 * @param <request> - NSMutableDictionary with a "username" key and value
 *
 * @return PFUser
 */
Parse.Cloud.define("userByUsername", function(request, response) {
	var query = new Parse.Query(Parse.User);
	var params = request.params;
	query.equalTo("username", params["username"]);
	query.first({
		success: function(result) {
			response.success(result);
		},
		error: function(error) {
			response.error(error);
			alert("Error: " + error.code + " " + error.message);
		}
	});
});

/**
 * @method userByEmail
 *
 * Returns Parse User if there is a user with that email, else nil
 * @note Does not send an error if there is no result
 *
 * @param <request> - NSMutableDictionary with a "email" key and value
 *
 * @return PFUser
 */
Parse.Cloud.define("userByEmail", function(request, response) {
	var query = new Parse.Query(Parse.User);
	var params = request.params;
	query.equalTo("email", params["email"]);
	query.first({
		success: function(result) {
			response.success(result);
		},
		error: function(error) {
			alert("Error: " + error.code + " " + error.message);
		}
	});
});

/**
 * @method afterSave for first save of User
 *
 * Initializes all unspecified fields to non-undefined values
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.afterSave(Parse.User, function(request) {
    Parse.Cloud.useMasterKey();
    var createdAt = request.object.get("createdAt");
    var updatedAt = request.object.get("updatedAt");
    var objectExisted = (createdAt.getTime() == updatedAt.getTime());
	if (objectExisted) {
		//Not sure if I need this
		//Parse.Cloud.useMasterKey();
		//Also not sure if relations are intialized as undefined on User save automatically
		//or if it already creates an empty one
		var user = request.object;
		var groups = user.relation("groups");
		var items = user.relation("items");
        user.set("description", "Ready to make my bucket list a reality!");
        user.set("numFulfilledIdeas", 0);
        user.set("itemCounter", 0);
        user.set("groupCounter", 0);
        user.save(null, {
            success: function(user) {
                // Execute any logic that should take place after the object is saved.
                alert("New object created with objectId: " + user.id);
            },
            error: function(user, error) {
                // Execute any logic that should take place if the save fails.
                // error is a Parse.Error with an error code and message.
                alert("Failed to create new object, with error code: " + error.message);
            }
        });
	}
});

/**
 * @method afterSave for first save of Group
 *
 * Initializes all unspecified fields to non-undefined values
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.afterSave("Group", function(request) {
    Parse.Cloud.useMasterKey();
    var createdAt = request.object.get("createdAt");
    var updatedAt = request.object.get("updatedAt");
    var objectExisted = (createdAt.getTime() == updatedAt.getTime());
    if (objectExisted) {
        //Not sure if I need this
        //Parse.Cloud.useMasterKey();
        //Also not sure if relations are intialized as undefined on User save automatically
        //or if it already creates an empty one
        var group = request.object;
        //var groups = user.relation("groups");
        //var items = user.relation("items");
        group.set("description", "Come out and be a part of any of our awesome events!");
        group.set("numFulfilledIdeas", 0);
        group.set("userCounter", 0);
        group.set("itemCounter", 0);

        group.save(null, {
            success: function(group) {
                // Execute any logic that should take place after the object is saved.
                alert("New object created with objectId: " + group.id);
            },
            error: function(group, error) {
                // Execute any logic that should take place if the save fails.
                // error is a Parse.Error with an error code and message.
                alert("Failed to create new object, with error code: " + error.message);
            }
        });
    }
});

/**
 * @method afterSave for first save of Group
 *
 * Initializes all unspecified fields to non-undefined values
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.afterSave("Item", function(request) {
    Parse.Cloud.useMasterKey();
    var createdAt = request.object.get("createdAt");
    var updatedAt = request.object.get("updatedAt");
    var objectExisted = (createdAt.getTime() == updatedAt.getTime());
    if (objectExisted) {
        //Not sure if I need this
        //Parse.Cloud.useMasterKey();
        //Also not sure if relations are intialized as undefined on User save automatically
        //or if it already creates an empty one
        var item = request.object;
        //var groups = user.relation("groups");
        //var items = user.relation("items");
        item.set("likes", 0);

        item.save(null, {
            success: function(item) {
                // Execute any logic that should take place after the object is saved.
                alert("New object created with objectId: " + item.id);
            },
            error: function(item, error) {
                // Execute any logic that should take place if the save fails.
                // error is a Parse.Error with an error code and message.
                alert("Failed to create new object, with error code: " + error.message);
            }
        });
    }
});

/**
 * @method createItem
 *
 * Creates an item with a title
 * TODO
 * Add other item parameters
 * @note does not add item to the user's items relation
 *
 * @param <request> - NSMutableDictionary with a "title" key and value
 *
 * @return Item
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("createItem", function(request, response) {
    var params = request.params;

    var Item = Parse.Object.extend("Item");
    var item = new Item();

    item.set("title", params["title"]);

    item.save(null, {
        success: function(item) {
            // Execute any logic that should take place after the object is saved.
            alert("New object created with objectId: " + item.id);
            response.success(item);
        },
        error: function(item, error) {
            // Execute any logic that should take place if the save fails.
            // error is a Parse.Error with an error code and message.
            alert("Failed to create new object, with error code: " + error.message);
        }
    });
});

/**
 * @method addItemToUser
 *
 * Adds item to a user's item relation
 *
 * @param <request> - NSMutableDictionary with a "itemId" key and value and a "userId" key and value
 *
 * @return User
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("addItemToUser", function(request, response) {
    //Should probably remove Master Key later
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var userId = params["userId"];
    var itemId = params["itemId"];
    var user;
    var item;

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
       user = result;

        var itemQuery = new Parse.Query("Item");
        itemQuery.equalTo("objectId", itemId);

        return itemQuery.first();
    }).then(function(result) {
        item = result;

        var items = user.relation("items");
        items.add(item);
        user.increment("itemCounter");
        return user.save();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in addItemToUser Promises: " + error.message);
    });
});


/**
 * @method addItemToGroup
 *
 * Adds item to a group's item relation
 *
 * @param <request> - NSMutableDictionary with a "itemId" key and value and a "groupId" key and value
 *
 * @return Group
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("addItemToGroup", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var groupId = params["groupId"];
    var itemId = params["itemId"];
    var group;
    var item;

    var groupQuery = new Parse.Query("Group");
    groupQuery.equalTo("objectId", groupId);
    groupQuery.first().then(function(result) {
        group = result;

        var itemQuery = new Parse.Query("Item");
        itemQuery.equalTo("objectId", itemId);

        return itemQuery.first();
    }).then(function(result) {
        item = result;

        var items = group.relation("items");
        items.add(item);

        group.increment("itemCounter");
        return group.save();
    }).then(function(result) {
        response.success(group);
    }, function(error) {
        alert("Failed in addItemToGroup Promises: " + error.message);
    });
});

/**
 * @method createGroup
 *
 * Creates a group with a group name
 * TODO
 * Add other group parameters
 * @note does not add group into a user's groups relation
 *
 * @param <request> - NSMutableDictionary with a "groupName" key and value
 *
 * @return Group
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("createGroup", function(request, response) {
    var params = request.params;

    var Group = Parse.Object.extend("Group");
    var group = new Group();

    group.set("groupName", params["groupName"]);

    group.save(null, {
        success: function(group) {
            // Execute any logic that should take place after the object is saved.
            alert("New object created with objectId: " + group.id);
            response.success(group);
        },
        error: function(group, error) {
            // Execute any logic that should take place if the save fails.
            // error is a Parse.Error with an error code and message.
            alert("Failed to create new object, with error code: " + error.message);
        }
    });
});

/**
 * @method addUserToGroup
 *
 * Adds group to a user's relation
 *
 * @param <request> - NSMutableDictionary with a "userId" key and value and a "groupId" key and value
 *
 * @return User
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("addUserToGroup", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var userId = params["userId"];
    var groupId = params["groupId"];
    var user;
    var group;

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
        user = result;

        var groupQuery = new Parse.Query("Group");
        groupQuery.equalTo("objectId", groupId);

        return groupQuery.first();
    }).then(function(result) {
        group = result;

        var groups = user.relation("groups");
        groups.add(group);

        user.increment("groupCounter");
        group.increment("userCounter");
        return user.save();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in addUserToGroup Promises: " + error.message);
    });
});
/**
 * @method getUserGroups
 *
 * Queries users and gets the groups user is in
 *
 * @param <request> - NSMutableDictionary with a "userId" key and value
 *
 * @return Array of Group of the user
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("getUserGroups", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var userId = params["userId"];
    var user;

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
        user = result;

        var groups = user.relation("groups");

        return groups.query().find();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in getUserGroups Promises: " + error.message);
    });
});

/**
 * @method getUserItems
 *
 * Queries users and gets the items the user has on his or her bucket list
 *
 * @param <request> - NSMutableDictionary with a "userId" key and value
 *
 * @return ArrayList of Item of the user
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("getUserItems", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var userId = params["userId"];
    var user;

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
        user = result;

        var items = user.relation("items");
        var itemsQuery = items.query();
        itemsQuery.descending("createdAt");
        return itemsQuery.find();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in getUserItems Promises: " + error.message);
    });
});

/**
 * @method getGroupItems
 *
 * Queries groups and gets the items the user has on his or her bucket list
 *
 * @param <request> - NSMutableDictionary with a "groupId" key and value
 *
 * @return ArrayList of Item of the group
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("getGroupItems", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var groupId = params["groupId"];
    var group;

    var groupQuery = new Parse.Query("Group");
    groupQuery.equalTo("objectId", groupId);
    groupQuery.first().then(function(result) {
        group = result;

        var items = group.relation("items");

        return items.query().find();
    }).then(function(result) {
        response.success(group);
    }, function(error) {
        alert("Failed in getGroupItems Promises: " + error.message);
    });
});


/**
 * @method getGroupUsers
 *
 * Queries users and gets which users are in the group
 *
 * @param <request> - NSMutableDictionary with a "groupId" key and value
 *
 * @return ArrayList of Parse.User that are in the group
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("getGroupUsers", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var groupId = params["groupId"];
    var group;

    var groupQuery = new Parse.Query("Group");
    groupQuery.equalTo("objectId", groupId);
    groupQuery.first().then(function(result) {
        group = result;

        var query = new Parse.Query(Parse.User);

        query.equalTo("groups", group);

        return query.find();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in getGroupUsers Promises: " + error.message);
    });
});

/**
 * @method getGroupByGroupName
 *
 * Queries groups by group name
 *
 * @param <request> - NSMutableDictionary with a "groupName" key and value
 *
 * @return Group
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("getGroupByGroupName", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var groupName = params["groupName"];

    var groupQuery = new Parse.Query("Group");
    groupQuery.equalTo("groupName", groupName);
    groupQuery.first().then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in getGroupByGroupName- Promises: " + error.message);
    });
});

/**
 * @method searchGroup
 *
 * Queries groups for what groups matches a search text
 *
 * @param <request> - NSMutableDictionary with a "searchText" key and value
 *
 * @return ArrayList of Group that satisfy the search text
 *
 * TODO
 * TEST FUNCTIONALITY
 */
Parse.Cloud.define("searchGroups", function(request, response) {
    Parse.Cloud.useMasterKey();

    var params = request.params;
    var searchText = params["searchText"];

    var groupQuery = new Parse.Query("Group");

    groupQuery.find().then(function(results) {
        var searchResults = [];
        var groups = results;
        for (var i = 0; i < groups.length; i++) {
            if (groups[i].get("groupName").indexOf(searchText) > -1) {
                searchResults.push(groups[i]);
            }
        }
        response.success(searchResults);
    }, function(error) {
        alert("Failed in searchGroup- Promises: " + error.message);
    });
});

/**
 * @method getUserItemCount
 *
 * Queries user and gets how many items he or she has in the items relation
 *
 * @param <request> - NSMutableDictionary with a "userId" key and value
 *
 * @return Int
 *
 * TODO
 * TEST FUNCTIONALITY
 */
//Parse.Cloud.define("getUserInfo", function(request, response) {
//    Parse.Cloud.useMasterKey();
//
//    var params = request.params;
//    var userId = params["userId"];
//
//    var userInfo = [];
//
//    var userQuery = new Parse.Query(Parse.User);
//    userQuery.equalTo("objectId", userId);
//    userQuery.first().then(function(result) {
//        user = result;
//
//        userInfo.push({
//            key: "username",
//            value: user.get("username")
//        });
//
//        userInfo.push({
//            key: "groupCounter",
//            value: user.get("groupCounter")
//        });
//
//
//        userInfo.push({
//            key: "itemCounter",
//            value: user.get("itemCounter")
//        });
//
//        userInfo.push({
//            key: "numFulfilledIdeas",
//            value: user.get("numFulfilledIdeas")
//        });
//
//        response.success(userInfo);
//    },  function(error) {
//        alert("Failed in getUserItemCount- Promises: " + error.message);
//    });
//});

Parse.Cloud.define("getUserByUserId", function(request, response) {
    Parse.Cloud.useMasterKey();

    var params = request.params;
    var userId = params["userId"];

    var userInfo = [];

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
        user = result;
        response.success(user);
    },  function(error) {
        alert("Failed in getUserByUserId- Promises: " + error.message);
    });
});

Parse.Cloud.define("removeItemFromUser", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var userId = params["userId"];
    var itemId = params["itemId"];
    var user;
    var item;

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
       user = result;

        var itemQuery = new Parse.Query("Item");
        itemQuery.equalTo("objectId", itemId);

        return itemQuery.first();
    }).then(function(result) {
        item = result;

        var items = user.relation("items");
        items.remove(item);
        user.set("itemCounter", user.get("itemCounter")-1);
        return user.save();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in removeItemFromUser Promises: " + error.message);
    });
})

Parse.Cloud.define("userFinishedItem", function(request, response) {
    Parse.Cloud.useMasterKey();
    var params = request.params;
    var userId = params["userId"];
    var itemId = params["itemId"];
    var user;
    var item;

    var userQuery = new Parse.Query(Parse.User);
    userQuery.equalTo("objectId", userId);
    userQuery.first().then(function(result) {
       user = result;

        var itemQuery = new Parse.Query("Item");
        itemQuery.equalTo("objectId", itemId);

        return itemQuery.first();
    }).then(function(result) {
        item = result;

        var items = user.relation("items");
        items.remove(item);
        user.increment("numFulfilledIdeas");
        return user.save();
    }).then(function(result) {
        response.success(result);
    }, function(error) {
        alert("Failed in removeItemFromUser Promises: " + error.message);
    });
})