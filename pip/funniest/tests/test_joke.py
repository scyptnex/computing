from unittest import TestCase

import funniest


class TestJoke(TestCase):

    def test_is_string(self):
        s = funniest.joke()
        self.assertTrue(isinstance(s, str))

    def test_is_nonempty(self):
        s = funniest.joke()
        self.assertTrue(len(s) > 0)
