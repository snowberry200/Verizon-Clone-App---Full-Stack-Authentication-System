class SharedAuthData {
  static String? lastEmail;
  static String? lastPassword;

  static void clear() {
    lastEmail = null;
    lastPassword = null;
  }
}
